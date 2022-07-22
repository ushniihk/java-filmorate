package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class RecommendationService {
    private final FilmService filmService;

    @Autowired
    public RecommendationService(FilmService filmService) {
        this.filmService = filmService;
    }

    public void compute(int maxSizeOfRecommendations) {
        Map<Long, Set<Long>> usersLikes = likeStorage.getUsersLikesMap();
        Map<Long, Set<Long>> similarUsers = new HashMap<>();

        // находим пользователей с макс. кол-вом общих лайков фильмам
        for (Map.Entry<Long, Set<Long>> i : usersLikes.entrySet()) {
            for (Map.Entry<Long, Set<Long>> j : usersLikes.entrySet()) {
                if (Objects.equals(i.getKey(), j.getKey())) continue;

                Set<Long> intersect = new HashSet<>(i.getValue());
                intersect.retainAll(j.getValue());

                if (intersect.size() > 0 && j.getValue().size() > i.getValue().size()) {
                    similarUsers.merge(i.getKey(), new HashSet<>(Set.of(j.getKey())), (oldVal, newVal) -> {
                        oldVal.add(j.getKey());
                        return oldVal;
                    });
                }
            }
        }

        // собираем фильмы которые не лайк пользователь, но лайкали друзья
        Map<Long, Set<Long>> usersRecommendations = new HashMap<>();
        for (Map.Entry<Long, Set<Long>> i : similarUsers.entrySet()) {
            Set<Long> currentUserFilms = usersLikes.get(i.getKey());
            Set<Long> recommendations = new HashSet<>();
            for (Long userId : i.getValue()) {
                if (recommendations.size() > maxSizeOfRecommendations) break;
                Set<Long> userFilms = usersLikes.get(userId);
                userFilms.removeAll(currentUserFilms);
                recommendations.addAll(userFilms);
            }
            usersRecommendations.put(i.getKey(), recommendations);
        }

        for (Map.Entry<Long, Set<Long>> i : usersRecommendations.entrySet()) {
            recommendationStorage.save(Recommendation.builder().whomId(i.getKey()).filmsIds(i.getValue()).build());
        }
    }

    /**
     * Получает рекомендации фильмов для пользователя.
     *
     * @param id уникальный идентификатор польз.
     * @return список рекомендованных фильмов
     */
    public List<Film> getFilmRecommendationsByUserId(Long id) {
        return recommendationStorage.getRecommendationByUserId(id).getFilmsIds().stream().map(filmService::getFilm)
                .collect(Collectors.toList());
    }

    @EventListener
    public void handleFilmLikeAdded(FilmLikeAddedEvent event) {
        this.compute(5);
    }
}
