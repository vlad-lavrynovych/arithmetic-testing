package com.example.arithmetic_testing.repos;

import com.example.arithmetic_testing.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface QuestionRepo extends JpaRepository<Question, Integer> {
    @Transactional
    void  deleteAllByGivenAnswerNull();
    //for last day
    @Query(value = "SELECT SUM(duration )/COUNT(duration) FROM questions WHERE user_id=?1  AND questions.answer_time >= date_trunc('Day',Now()) - interval '0 days' ", nativeQuery = true)
    Double selectdayAvg(Integer id);

    @Query(value = "SELECT max(questions.duration) FROM questions WHERE user_id=?1 AND questions.duration!=0 AND questions.answer_time >= date_trunc('Day',Now()) - interval '0 days' ", nativeQuery = true)
    Integer selectdayMax(Integer id);

    @Query(value = "SELECT min(questions.duration) FROM questions WHERE user_id=?1 AND questions.duration!=0  AND questions.answer_time >= date_trunc('Day',Now()) - interval '0 days'", nativeQuery = true)
    Integer selectdayMin(Integer id);

    @Query(value = "SELECT count(questions.id) FROM questions WHERE user_id=?1  AND questions.answer_time >= date_trunc('Day',Now()) - interval '0 days'", nativeQuery = true)
    Integer selectdayEnters(Integer id);

    @Query(value = "SELECT count(questions.id) FROM questions WHERE user_id=?1 AND questions.answer_time >= date_trunc('Day',Now()) - interval '0 days'", nativeQuery = true)
    Integer amountquestionsday(Integer id);

    @Query(value = "SELECT count(questions.id) FROM questions WHERE user_id=?1 AND questions.is_correct=true and questions.answer_time >= date_trunc('Day',Now()) - interval '0 days' ", nativeQuery = true)
    Integer amountquestionscorectday(Integer id);

    //for last month
    @Query(value = "SELECT SUM(questions.duration )/COUNT(duration) FROM questions WHERE user_id=?1 AND questions.answer_time >= date_trunc('Day',Now()) - interval '30 days' ", nativeQuery = true)
    Double selectmonthAvg(Integer id);

    @Query(value = "SELECT max(questions.duration) FROM questions WHERE user_id=?1 AND questions.duration!=0 AND questions.answer_time >= date_trunc('Day',Now()) - interval '30 days' ", nativeQuery = true)
    Integer selectmonthMax(Integer id);

    @Query(value = "SELECT min(questions.duration) FROM questions WHERE user_id=?1 AND questions.duration!=0  AND questions.answer_time >= date_trunc('Day',Now()) - interval '30 days'", nativeQuery = true)
    Integer selectmonthMin(Integer id);


    @Query(value = "SELECT count(questions.id) FROM questions WHERE user_id=?1 AND questions.answer_time >= date_trunc('Day',Now()) - interval '30 days'", nativeQuery = true)
    Integer amountquestionsmonth(Integer id);

    @Query(value = "SELECT count(questions.id) FROM questions WHERE user_id=?1 and questions.is_correct=true AND questions.answer_time >= date_trunc('Day',Now()) - interval '30 days' ", nativeQuery = true)
    Integer amountquestionscorectmonth(Integer id);


    //for last year
    @Query(value = "SELECT SUM(duration )/COUNT(duration) FROM questions WHERE user_id=?1 AND questions.answer_time >= date_trunc('Day',Now()) - interval '360 days' ", nativeQuery = true)
    Double selectyearAvg(Integer id);

    @Query(value = "SELECT max(questions.duration) FROM questions WHERE user_id=?1 AND questions.duration!=0 AND questions.answer_time >= date_trunc('Day',Now()) - interval '360 days' ", nativeQuery = true)
    Integer selectyearMax(Integer id);

    @Query(value = "SELECT min(questions.duration) FROM questions WHERE user_id=?1 AND questions.duration!=0  AND questions.answer_time >= date_trunc('Day',Now()) - interval '360 days'", nativeQuery = true)
    Integer selectyearMin(Integer id);

    @Query(value = "SELECT count(questions.id) FROM questions WHERE user_id=?1 AND questions.answer_time >= date_trunc('Day',Now()) - interval '360 days'", nativeQuery = true)
    Integer amountquestionsyear(Integer id);

    @Query(value = "SELECT count(questions.id) FROM questions WHERE user_id=?1 and  questions.is_correct=true AND questions.answer_time >= date_trunc('Day',Now()) - interval '360 days' ", nativeQuery = true)
    Integer amountquestionscorectyear(Integer id);
}
