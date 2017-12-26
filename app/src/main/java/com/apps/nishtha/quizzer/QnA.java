package com.apps.nishtha.quizzer;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nishtha on 24/12/17.
 */

public class QnA implements Serializable{
    private String text;
    private List<Answers> answers;

    public String getText() {
        return text;
    }

    public List<Answers> getAnswers() {
        return answers;
    }
}
