package ru.yandex.practicum.filmorate.storage.recomemendations;

public interface RecomendationsDao {

    Recommendation getRecommendationById(Long userId);

}
