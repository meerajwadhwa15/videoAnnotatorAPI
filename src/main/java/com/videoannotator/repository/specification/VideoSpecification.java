package com.videoannotator.repository.specification;

import com.videoannotator.model.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class VideoSpecification {

    public Specification<Video> filterByConditions(User user, List<SubCategory> subCategories, List<UserPlaylist> userPlaylists, String keyword) {
        return (videoRoot, query, criteriaBuilder) -> {
            Join<Video, SubCategory> subCategoryJoin = videoRoot.join("subCategory");
            Join<SubCategory, Category> categoryJoin = subCategoryJoin.join("category");

            String pattern = "%" + keyword + "%";
            Predicate predicate = criteriaBuilder.or(
                    criteriaBuilder.or(
                            criteriaBuilder.like(videoRoot.get("name"), pattern),
                            criteriaBuilder.like(videoRoot.get("description"), pattern)
                    ),
                    criteriaBuilder.like(subCategoryJoin.get("name"), pattern),
                    criteriaBuilder.like(categoryJoin.get("name"), pattern));
            if (user != null) {
                Join<Video, User> userJoin = videoRoot.join("userList");
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(userJoin.get("id"), user.getId()));
            }
            if (!subCategories.isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.in(subCategoryJoin.get("id")).value(subCategories.stream().map(SubCategory::getId).collect(Collectors.toList())));
            }
            if (null != userPlaylists) {
                Join<Video, UserPlaylist> playlistJoin = videoRoot.join("playlists");
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.in(playlistJoin.get("id")).value(userPlaylists.stream().map(UserPlaylist::getId).collect(Collectors.toList())));
            }
            return predicate;
        };
    }
}
