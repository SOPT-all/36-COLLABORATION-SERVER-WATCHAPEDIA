package or.sopt.soptwatcha.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import or.sopt.soptwatcha.common.response.BaseResponse;
import or.sopt.soptwatcha.dto.request.CommentCreateRequestDTO;
import or.sopt.soptwatcha.dto.response.CommentCreateResponseDTO;
import or.sopt.soptwatcha.dto.response.CommentResponseDTO;
import or.sopt.soptwatcha.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{postId}/comments")
    public ResponseEntity<BaseResponse<CommentResponseDTO>> getTopLikedComment(@PathVariable("postId") Long movieId) {
        CommentResponseDTO responseDto = commentService.getTopLikedComment(movieId);
        return ResponseEntity.ok(BaseResponse.ok(responseDto));

    }
  
    @PostMapping("/{postId}/comments")
    public ResponseEntity<BaseResponse<CommentCreateResponseDTO>> createComment(
            @PathVariable Long postId,
            @RequestBody @Valid CommentCreateRequestDTO request) {
        
        CommentCreateResponseDTO response = commentService.createComment(postId, request);
        return ResponseEntity.ok(BaseResponse.ok(response));
    }
}