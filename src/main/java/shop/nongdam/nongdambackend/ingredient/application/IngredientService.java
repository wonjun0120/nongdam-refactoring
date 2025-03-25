package shop.nongdam.nongdambackend.ingredient.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import shop.nongdam.nongdambackend.auth.exception.EmailNotFoundException;
import shop.nongdam.nongdambackend.farm.domain.Farm;
import shop.nongdam.nongdambackend.farm.domain.repository.FarmRepository;
import shop.nongdam.nongdambackend.farm.exception.FarmAccessDeniedException;
import shop.nongdam.nongdambackend.farm.exception.FarmNotFoundException;
import shop.nongdam.nongdambackend.global.aws.application.ImageService;
import shop.nongdam.nongdambackend.global.dto.PageInfoResDto;
import shop.nongdam.nongdambackend.ingredient.api.dto.request.IngredientSaveRequestDTO;
import shop.nongdam.nongdambackend.ingredient.api.dto.response.IngredientGptCommentDto;
import shop.nongdam.nongdambackend.ingredient.api.dto.response.IngredientInfoResponseDTO;
import shop.nongdam.nongdambackend.ingredient.api.dto.response.IngredientInfoResponseDTOs;
import shop.nongdam.nongdambackend.ingredient.domain.*;
import shop.nongdam.nongdambackend.ingredient.domain.repository.IngredientCategoryRepository;
import shop.nongdam.nongdambackend.ingredient.domain.repository.IngredientRepository;
import shop.nongdam.nongdambackend.ingredient.domain.repository.IngredientUglyReasonRepository;
import shop.nongdam.nongdambackend.ingredient.exception.IngredientCategoryNotFoundException;
import shop.nongdam.nongdambackend.ingredient.exception.IngredientNotFoundException;
import shop.nongdam.nongdambackend.ingredient.exception.IngredientUglyReasonNotFoundException;
import shop.nongdam.nongdambackend.member.domain.Member;
import shop.nongdam.nongdambackend.member.domain.repository.MemberRepository;
import shop.nongdam.nongdambackend.openai.application.IngredientAnalysisScript;
import shop.nongdam.nongdambackend.openai.application.OpenAiService;
import shop.nongdam.nongdambackend.region.domain.Region;
import shop.nongdam.nongdambackend.region.domain.repository.RegionRepository;
import shop.nongdam.nongdambackend.region.exception.RegionNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class IngredientService {
    private final ImageService imageService;
    private final OpenAiService openAiService;

    private final IngredientRepository ingredientRepository;
    private final MemberRepository memberRepository;
    private final FarmRepository farmRepository;
    private final IngredientUglyReasonRepository ingredientUglyReasonRepository;
    private final IngredientCategoryRepository ingredientCategoryRepository;
    private final RegionRepository regionRepository;

    @Transactional
    public IngredientInfoResponseDTO saveIngredientInfo(
            String email,
            Long farmId,
            IngredientSaveRequestDTO ingredientSaveRequestDto,
            List<MultipartFile> ingredientImages
    ) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(EmailNotFoundException::new);

        Farm farm = farmRepository.findByMemberId(member.getId())
                .orElseThrow(FarmNotFoundException::new);

        if(!farm.getId().equals(farmId)) throw new FarmAccessDeniedException();

        Ingredient ingredient = buildNewIngredient(farm, ingredientSaveRequestDto);
        farm.addIngredient(ingredient);
        ingredientImages.forEach(ingredientImage -> {
            String imageUrl = imageService.saveImage(ingredientImage);
            IngredientImage newIngredientImage = IngredientImage.builder()
                    .imageUrl(imageUrl)
                    .ingredient(ingredient)
                    .build();
            ingredient.addIngredientImage(newIngredientImage);
        });

        ingredientRepository.save(ingredient);

        return IngredientInfoResponseDTO.from(ingredient);
    }

    private Ingredient buildNewIngredient(Farm farm, IngredientSaveRequestDTO ingredientSaveRequestDto) {
        IngredientUglyReason ingredientUglyReason = ingredientUglyReasonRepository
                .findByName(ingredientSaveRequestDto.uglyReason())
                .orElseThrow(IngredientUglyReasonNotFoundException::new);

        IngredientCategory ingredientCategory = ingredientCategoryRepository
                .findByName(ingredientSaveRequestDto.ingredientCategory())
                .orElseThrow(IngredientCategoryNotFoundException::new);

        return Ingredient.builder()
                .farm(farm)
                .ingredientName(ingredientSaveRequestDto.ingredientName())
                .ingredientUglyReason(ingredientUglyReason)
                .ingredientDescription(ingredientSaveRequestDto.ingredientDescription())
                .ingredientCategory(ingredientCategory)
                .price(ingredientSaveRequestDto.price())
                .build();
    }

    public IngredientInfoResponseDTO findById(Long ingredientId) {
        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(IngredientNotFoundException::new);

        return IngredientInfoResponseDTO.from(ingredient);
    }

    public IngredientInfoResponseDTOs findAll(String category, String region, Pageable pageable) {
        Page<Ingredient> ingredientsPage = ingredientRepository.findAllIngredients(pageable);
        List<Ingredient> ingredients = ingredientsPage.getContent();

        if (!category.equals("전체") || !region.equals("전체")) {
            if (!category.equals("전체")) {
                IngredientCategory matchedCategory = ingredientCategoryRepository.findByName(category)
                        .orElseThrow(IngredientCategoryNotFoundException::new);

                ingredients = ingredients.stream()
                        .filter(ingredient -> ingredient.getIngredientCategory().equals(matchedCategory))
                        .toList();
            }

            if (!region.equals("전체")) {
                Region matchedRegion = regionRepository.findByName(region)
                        .orElseThrow(RegionNotFoundException::new);

                ingredients = ingredients.stream()
                        .filter(ingredient -> ingredient.getFarm().getRegion().equals(matchedRegion))
                        .toList();
            }
        }

        List<IngredientInfoResponseDTO> ingredientInfoResponseDTOs = ingredients.stream()
                .map(IngredientInfoResponseDTO::from)
                .toList();

        return IngredientInfoResponseDTOs.of(ingredientInfoResponseDTOs, PageInfoResDto.from(ingredientsPage));
    }

    @Transactional
    public IngredientGptCommentDto chatGpt(Long ingredientId) {
        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(IngredientNotFoundException::new);

        String gptComment = ingredient.getGptComment();

        if (gptComment.isBlank()) {
            String script = IngredientAnalysisScript
                    .script(ingredient.getFarm().getRegion().getName(), ingredient.getIngredientName());

            String newGptComment = openAiService.chat(script);
            ingredient.setGptComment(newGptComment);

            log.info(ingredient.getGptComment());

            ingredientRepository.save(ingredient);
            gptComment = newGptComment;
        }

        return IngredientGptCommentDto.from(gptComment);
    }
}
