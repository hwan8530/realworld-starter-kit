package com.realworld.backend.article.service;

import com.realworld.backend.article.dto.ResponseTag;
import com.realworld.backend.article.entity.Tag;
import com.realworld.backend.article.repository.TagRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class TagService {

  private final TagRepository tagRepository;

  public ResponseTag listOfTags() {
    List<Tag> tagList = tagRepository.findAll();
    List<String> tagNameList = new ArrayList<>();
    for (Tag tag : tagList) {
      tagNameList.add(tag.getTagName());
    }
    return new ResponseTag(tagNameList);
  }
}
