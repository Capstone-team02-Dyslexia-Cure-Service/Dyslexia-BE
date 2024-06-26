package com.capstone.dyslexia.domain.test.application;

import com.capstone.dyslexia.domain.dateAchievement.application.DateAchievementService;
import com.capstone.dyslexia.domain.level.application.LevelRangeService;
import com.capstone.dyslexia.domain.member.application.MemberService;
import com.capstone.dyslexia.domain.member.domain.Member;
import com.capstone.dyslexia.domain.member.domain.repository.MemberRepository;
import com.capstone.dyslexia.domain.question.application.QuestionService;
import com.capstone.dyslexia.domain.question.domain.Question;
import com.capstone.dyslexia.domain.question.domain.QuestionResponseType;
import com.capstone.dyslexia.domain.question.domain.sentence.QuestionSentence;
import com.capstone.dyslexia.domain.question.domain.sentence.repository.QuestionSentenceRepository;
import com.capstone.dyslexia.domain.question.domain.word.QuestionWord;
import com.capstone.dyslexia.domain.question.domain.word.repository.QuestionWordRepository;
import com.capstone.dyslexia.domain.question.dto.response.QuestionResponseDto;
import com.capstone.dyslexia.domain.solvingRecord.application.SolvingRecordService;
import com.capstone.dyslexia.domain.solvingRecord.domain.SolvingRecord;
import com.capstone.dyslexia.domain.solvingRecord.domain.repository.SolvingRecordRepository;
import com.capstone.dyslexia.domain.solvingRecord.dto.request.SolvingRecordRequestDto;
import com.capstone.dyslexia.domain.test.dto.TestResponseDto;
import com.capstone.dyslexia.domain.test.domain.Test;
import com.capstone.dyslexia.domain.test.domain.repository.TestRepository;
import com.capstone.dyslexia.domain.testQuestionWord.domain.repository.TestQuestionWordRepository;
import com.capstone.dyslexia.domain.testQuestionWord.domain.TestQuestionWord;
import com.capstone.dyslexia.domain.testQuestionsentence.domain.TestQuestionSentence;
import com.capstone.dyslexia.domain.testQuestionsentence.domain.repository.TestQuestionSentenceRepository;
import com.capstone.dyslexia.global.error.exceptions.BadRequestException;
import com.capstone.dyslexia.global.error.exceptions.InternalServerException;
import com.capstone.dyslexia.global.error.exceptions.ServiceUnavailableException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static com.capstone.dyslexia.global.error.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TestService {

    private final TestRepository testRepository;
    private final MemberService memberService;
    private final QuestionService questionService;
    private final TestQuestionWordRepository testQuestionWordRepository;
    private final TestQuestionSentenceRepository testQuestionSentenceRepository;
    private final SolvingRecordRepository solvingRecordRepository;
    private final SolvingRecordService solvingRecordService;
    private final DateAchievementService dateAchievementService;

    @Transactional
    public TestResponseDto.Create createTest(Long memberId, Long numOfQuestions) {
        Member member = memberService.memberValidation(memberId);

        Map<Question, QuestionResponseType> questionMap = questionService.randomQuestionListBuilder(member, numOfQuestions);

        if (questionMap.size() != numOfQuestions) {
            throw new InternalServerException(INTERNAL_SERVER, "문제를 생성 후 전달하는 도중 오류가 발생했습니다.");
        }

        List<TestQuestionWord> testQuestionWordList = new ArrayList<>();
        List<TestQuestionSentence> testQuestionSentenceList = new ArrayList<>();

        List<QuestionResponseDto.GetRandom> questionResponseDtoList = new ArrayList<>();

        while (!questionMap.isEmpty()) {
            Question question = questionMap.keySet().iterator().next();
            if (question instanceof QuestionWord) {
                TestQuestionWord testQuestionWord = TestQuestionWord.builder()
                        .questionWord((QuestionWord) question)
                        .questionResponseType(questionMap.get(question))
                        .isCorrect(false)
                        .build();

                testQuestionWord = testQuestionWordRepository.save(testQuestionWord);
                testQuestionWordList.add(testQuestionWord);

                questionResponseDtoList.add(QuestionResponseDto.GetRandom.from((QuestionWord) question, questionMap.get(question)));

                questionMap.remove(question);
            } else if (question instanceof QuestionSentence) {
                TestQuestionSentence testQuestionSentence = TestQuestionSentence.builder()
                        .questionSentence((QuestionSentence) question)
                        .questionResponseType(questionMap.get(question))
                        .isCorrect(false)
                        .build();

                testQuestionSentence = testQuestionSentenceRepository.save(testQuestionSentence);
                testQuestionSentenceList.add(testQuestionSentence);

                questionResponseDtoList.add(QuestionResponseDto.GetRandom.from((QuestionSentence) question));

                questionMap.remove(question);
            }
        }

        Test test = Test.builder()
                .member(member)
                .testQuestionWordList(testQuestionWordList)
                .testQuestionSentenceList(testQuestionSentenceList)
                .build();

        test = testRepository.save(test);

        return TestResponseDto.Create.from(test, questionResponseDtoList);
    }

    @Transactional
    public String interimSubmitWrite(Long memberId, Long testId, Long questionWordId, QuestionResponseType questionResponseType, String answer) {
        memberService.memberValidation(memberId);

        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new ServiceUnavailableException(DATA_NOT_EXIEST, "해당 테스트에 대한 데이터가 존재하지 않습니다."));

        List<TestQuestionWord> testQuestionWordList = test.getTestQuestionWordList().stream()
                .filter(testQuestionWord -> testQuestionWord.getQuestionWord().getId().equals(questionWordId))
                .toList();

        if (testQuestionWordList.size() != 1) {
            if (testQuestionWordList.isEmpty()) {
                throw new BadRequestException(INVALID_PARAMETER, "두 개 이상의 문제가 존재합니다.");
            }
            throw new InternalServerException(INTERNAL_SERVER, "해당 문제에 대한 데이터가 존재하지 않습니다.");
        }

        if (!(questionResponseType.equals(QuestionResponseType.WRITE_WORD) || questionResponseType.equals(QuestionResponseType.SELECT_WORD))) {
            throw new BadRequestException(INVALID_PARAMETER, "Invalid questionResponseType. It should be WRITE_WORD.");
        }

        SolvingRecord solvingRecord = solvingRecordService.createSolvingRecord(memberId, SolvingRecordRequestDto.CreateMerged.builder()
                .questionId(questionWordId)
                .questionResponseType(questionResponseType)
                .answer(answer)
                .build());

        testQuestionWordList.get(0).setInfo(solvingRecord);

        testQuestionWordRepository.save(testQuestionWordList.get(0));

        return "OK";
    }

    @Transactional
    public String interimSubmitRead(Long memberId, Long testId, Long questionId, QuestionResponseType questionResponseType, MultipartFile answerFile) {
        memberService.memberValidation(memberId);

        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new ServiceUnavailableException(DATA_NOT_EXIEST, "해당 테스트에 대한 데이터가 존재하지 않습니다."));

        if (questionResponseType.equals(QuestionResponseType.READ_WORD)) {
            List<TestQuestionWord> testQuestionWordList = test.getTestQuestionWordList().stream()
                    .filter(testQuestionWord -> testQuestionWord.getQuestionWord().getId().equals(questionId))
                    .toList();
            if (testQuestionWordList.size() != 1) {
                if (testQuestionWordList.isEmpty()) {
                    throw new InternalServerException(INTERNAL_SERVER, "해당 문제에 대한 데이터가 존재하지 않습니다.");
                }
                throw new BadRequestException(INVALID_PARAMETER, "두 개 이상의 문제가 존재합니다.");
            }

            SolvingRecord solvingRecord = solvingRecordService.createSolvingRecord(memberId, SolvingRecordRequestDto.CreateMerged.builder()
                    .questionId(questionId)
                    .questionResponseType(questionResponseType)
                    .answerFile(answerFile)
                    .build());

            testQuestionWordList.get(0).setInfo(solvingRecord);

            testQuestionWordRepository.save(testQuestionWordList.get(0));
        } else if (questionResponseType.equals(QuestionResponseType.READ_SENTENCE)) {
            List<TestQuestionSentence> testQuestionSentenceList = test.getTestQuestionSentenceList().stream()
                    .filter(testQuestionSentence -> testQuestionSentence.getQuestionSentence().getId().equals(questionId))
                    .toList();

            if (testQuestionSentenceList.size() != 1) {
                if (testQuestionSentenceList.isEmpty()) {
                    throw new InternalServerException(INTERNAL_SERVER, "해당 문제에 대한 데이터가 존재하지 않습니다.");
                }
                throw new BadRequestException(INVALID_PARAMETER, "두 개 이상의 문제가 존재합니다.");
            }

            SolvingRecord solvingRecord = solvingRecordService.createSolvingRecord(memberId, SolvingRecordRequestDto.CreateMerged.builder()
                    .questionId(questionId)
                    .questionResponseType(questionResponseType)
                    .answerFile(answerFile)
                    .build());

            testQuestionSentenceList.get(0).setInfo(solvingRecord);

            testQuestionSentenceRepository.save(testQuestionSentenceList.get(0));
        } else {
            throw new BadRequestException(INVALID_PARAMETER, "Invalid questionResponseType. It should be READ_WORD or READ_SENTENCE.");
        }

        return "OK";
    }

    @Transactional
    public Long submitTest(Long memberId, Long testId) {
        Member member = memberService.memberValidation(memberId);

        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new ServiceUnavailableException(DATA_NOT_EXIEST, "해당 테스트에 대한 데이터가 존재하지 않습니다."));

        List<SolvingRecord> solvingRecordList = new ArrayList<>();

        for (TestQuestionWord testQuestionWord : test.getTestQuestionWordList()) {
            SolvingRecord solvingRecord = SolvingRecord.convertTestQuestionWordToSolvingRecord(testQuestionWord, member);
            solvingRecordList.add(solvingRecord);
        }

        for (TestQuestionSentence testQuestionSentence : test.getTestQuestionSentenceList()) {
            SolvingRecord solvingRecord = SolvingRecord.convertTestQuestionSentenceToSolvingRecord(testQuestionSentence, member);
            solvingRecordList.add(solvingRecord);
        }

        Long wrongSolvingRecordCount = solvingRecordList.stream()
                .filter(solvingRecord -> !solvingRecord.getIsCorrect())
                .count();

        solvingRecordList = solvingRecordRepository.saveAll(solvingRecordList);

        dateAchievementService.addSolvingRecord(solvingRecordList);

        testRepository.delete(test);
        return wrongSolvingRecordCount;
    }
}
