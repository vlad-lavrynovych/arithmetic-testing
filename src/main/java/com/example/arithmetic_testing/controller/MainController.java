package com.example.arithmetic_testing.controller;

import com.example.arithmetic_testing.domain.Question;
import com.example.arithmetic_testing.domain.User;
import com.example.arithmetic_testing.repos.QuestionRepo;
import com.example.arithmetic_testing.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class MainController {
    @Autowired
    private QuestionRepo questionRepo;
    @Autowired
    private UserRepo userRepo;
    private List<Question> lastQuestions = new ArrayList<>();

    @GetMapping("/")
    public String greeting(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("username", user.getUsername());
        return "greeting.html";
    }

    @GetMapping("/endtesting")
    public String endtesting(Model model, @AuthenticationPrincipal User user) throws ParseException {
        questionRepo.deleteAllByGivenAnswerNull();
        lastQuestions.removeIf(s -> s.getGivenAnswer() == null);
        User user1 = userRepo.findById(user.getId()).get();

        int correct = (int) lastQuestions.stream().filter(Question::getIsCorrect).count();
        int incorrect = (int) lastQuestions.stream().filter(s -> !s.getIsCorrect()).count();

        double percentage = 0;
        if (!lastQuestions.isEmpty()) {
            percentage = ((int) 100 * correct / lastQuestions.size());
        }
        //for last day

        Double dayAvgAllTime = questionRepo.selectdayAvg(Math.toIntExact(user.getId()));
        Integer dayMaxAllTime = questionRepo.selectdayMax(Math.toIntExact(user.getId()));
        Integer dayMinAllTime = questionRepo.selectdayMin(Math.toIntExact(user.getId()));
        Integer dayNumberQuestions = questionRepo.amountquestionsday(Math.toIntExact(user.getId()));
        Integer dayNumberQuestionsCorrect = questionRepo.amountquestionscorectday(Math.toIntExact(user.getId()));
        double dayPercentage = ((double) (dayNumberQuestionsCorrect * 100) / dayNumberQuestions);

        //for last month

        Double monthAvgAllTime = questionRepo.selectmonthAvg(Math.toIntExact(user.getId()));
        Integer monthMaxAllTime = questionRepo.selectmonthMax(Math.toIntExact(user.getId()));
        Integer monthMinAllTime = questionRepo.selectmonthMin(Math.toIntExact(user.getId()));
        Integer monthNumberQuestions = questionRepo.amountquestionsmonth(Math.toIntExact(user.getId()));
        Integer monthNumberQuestionsCorrect
                = questionRepo.amountquestionscorectmonth(Math.toIntExact(user.getId()));
        double monthpercentage = ((double) (monthNumberQuestionsCorrect * 100) / monthNumberQuestions);

        //for last year

        Double yearAvgAllTime = questionRepo.selectyearAvg(Math.toIntExact(user.getId()));
        Integer yearMaxAllTime = questionRepo.selectyearMax(Math.toIntExact(user.getId()));
        Integer yearMinAllTime = questionRepo.selectyearMin(Math.toIntExact(user.getId()));
        Integer yearNumberofQustions = questionRepo.amountquestionsyear(Math.toIntExact(user.getId()));
        Integer yearNumberofQuestionsCorect = questionRepo.amountquestionscorectyear(Math.toIntExact(user.getId()));
        double yearpercentage = ((double) (yearNumberofQuestionsCorect * 100) / yearNumberofQustions);


        model.addAttribute("percent", percentage);

        model.addAttribute("max", lastQuestions.stream().mapToLong(Question::getDuration).max().getAsLong());
        model.addAttribute("min", lastQuestions.stream().mapToLong(Question::getDuration).min().getAsLong());
        model.addAttribute("avg", lastQuestions.stream().mapToLong(Question::getDuration).average().getAsDouble());
        model.addAttribute("count", (long) lastQuestions.size());
        model.addAttribute("durration_per_question", lastQuestions.stream().map(Question::getDuration).collect(Collectors.toList()));
        model.addAttribute("CorectAnswers", lastQuestions.stream().filter(Question::getIsCorrect).count());
        model.addAttribute("IncorectAnswers", lastQuestions.stream().filter(s -> !s.getIsCorrect()).count());

        //for all time
        model.addAttribute("TotalAvgAllTime", user1.getQuestions().stream().mapToLong(Question::getDuration).average().getAsDouble());
        model.addAttribute("TotalMinAllTime", user1.getQuestions().stream().mapToLong(Question::getDuration).min().getAsLong());
        model.addAttribute("TotalMaxAllTime", user1.getQuestions().stream().mapToLong(Question::getDuration).max().getAsLong());
        model.addAttribute("Questions", (long) user1.getQuestions().size());
        model.addAttribute("TotalNumberCorect", user1.getQuestions().stream().filter(Question::getIsCorrect).count());
        model.addAttribute("TotalPercent", (double) user1.getQuestions().stream().filter(s -> !s.getIsCorrect()).count() / user1.getQuestions().size() * 100);

        //for day
        model.addAttribute("dayAvgAllTime", dayAvgAllTime);
        model.addAttribute("dayMinAllTime", dayMinAllTime);
        model.addAttribute("dayMaxAllTime", dayMaxAllTime);
        model.addAttribute("dayQuestions", dayNumberQuestions);
        model.addAttribute("dayNumberCorect", dayNumberQuestionsCorrect);
        model.addAttribute("dayPercent", dayPercentage);

        //for month
        model.addAttribute("monthAvgAllTime", monthAvgAllTime);
        model.addAttribute("monthMinAllTime", monthMinAllTime);
        model.addAttribute("monthMaxAllTime", monthMaxAllTime);
        model.addAttribute("monthQuestions", monthNumberQuestions);
        model.addAttribute("monthNumberCorect", monthNumberQuestionsCorrect);
        model.addAttribute("monthPercent", monthpercentage);

        //for year
        model.addAttribute("yearAvgAllTime", yearAvgAllTime);
        model.addAttribute("yearMinAllTime", yearMinAllTime);
        model.addAttribute("yearMaxAllTime", yearMaxAllTime);
        model.addAttribute("yearQuestions", yearNumberofQustions);
        model.addAttribute("yearNumberCorect", yearNumberofQuestionsCorect);
        model.addAttribute("yearPercent", yearpercentage);
        lastQuestions = new ArrayList<>();
        return "endtesting.html";
    }


    @GetMapping("/main")
    public String main(Model model, @AuthenticationPrincipal User user) {

        User user1 = userRepo.findById(user.getId()).get();

        String expression;
        String[] GeneratedExpression = generateExpression();

        expression = GeneratedExpression[0];
        String solution = GeneratedExpression[1];

        Question question = new Question();
        question.setAppearTime(new Date());
        question.setUser(user1);
        question.setExpression(expression);
        question.setCorrectAnswer(Integer.parseInt(solution));
        questionRepo.save(question);
        user1.getQuestions().add(question);
        userRepo.save(user1);
        model.addAttribute("expression", expression);
        model.addAttribute("solution", solution);
        model.addAttribute("user_id", user.getId());
        model.addAttribute("user_name", user.getUsername());

        return "main.html";
    }

    @PostMapping("/main")
    public String giveAnswer(
            @AuthenticationPrincipal User user,
            @RequestParam String expression,
            @RequestParam String solution,
            @RequestParam String answer,
            Model model) {

        User user1 = userRepo.findById(user.getId()).get();
        Question question = user1.getQuestions().stream().filter(s -> s.getGivenAnswer() == null).findFirst().get();
        question.setGivenAnswer(Integer.valueOf(solution));
        question.setIsCorrect(solution.equals(answer));
        question.setAnswerTime(new Date());
        question.setDuration((question.getAnswerTime().getTime() - question.getAppearTime().getTime()) / 1000);
        lastQuestions.add(questionRepo.save(question));
        user1.getQuestions().add(question);
        userRepo.save(user1);
        return main(model, user);
    }

    private static String[] generateExpression() {
        String[] strings = new String[2];
        int firstNumber = ((int) (Math.random() * (1 - 100))) + 100;
        int secondNumber = ((int) (Math.random() * (1 - 100))) + 100;
        int operation = ((int) (Math.random() * (0 - 5))) + 5;
        String expression;
        int solution;
        if (operation == 1) {
            solution = firstNumber - secondNumber;
            String solutionstring = Integer.toString(solution);
            expression = firstNumber + " - " + secondNumber + " = ";
            strings[0] = expression;
            strings[1] = solutionstring;
            return strings;
        } else if (operation == 2) {
            solution = firstNumber + secondNumber;
            String solutionstring = Integer.toString(solution);
            expression = firstNumber + " + " + secondNumber + " = ";
            strings[0] = expression;
            strings[1] = solutionstring;
            return strings;
        } else if (operation == 3) {
            solution = firstNumber * secondNumber;
            String solutionstring = Integer.toString(solution);
            expression = firstNumber + " * " + secondNumber + " = ";
            strings[0] = expression;
            strings[1] = solutionstring;
            return strings;
        } else {
            solution = firstNumber / secondNumber;
            String solutionstring = Integer.toString(solution);
            expression = firstNumber + " / " + secondNumber + " = ";
            strings[0] = expression;
            strings[1] = solutionstring;
            return strings;
        }
    }
}
