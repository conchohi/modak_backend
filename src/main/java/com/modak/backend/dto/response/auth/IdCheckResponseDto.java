package com.modak.backend.dto.response.auth;

import com.modak.backend.common.ResponseCode;
import com.modak.backend.common.ResponseMessage;
import com.modak.backend.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class IdCheckResponseDto extends ResponseDto {

    private IdCheckResponseDto(){
        super();
    }

    public static ResponseEntity<? super ResponseDto> success(){
        IdCheckResponseDto responseBody = new IdCheckResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> duplicateId() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.DUPLICATE_ID, ResponseMessage.DUPLICATE_ID);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }
    
}
