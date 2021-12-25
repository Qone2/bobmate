package com.bobmate.bobmate.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberMeetRepositoryTest {

    @Autowired MemberMeetRepository memberMeetRepository;

    @Test
    public void deleteNull() throws Exception {
        //given


        //when


        //then
        assertThrows(Exception.class, () -> memberMeetRepository.delete(null));
    }

}
