package or.sopt.soptwatcha.controller;

import lombok.RequiredArgsConstructor;
import or.sopt.soptwatcha.common.response.BaseResponse;
import or.sopt.soptwatcha.dto.response.CommentResponseDTO;
import or.sopt.soptwatcha.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}