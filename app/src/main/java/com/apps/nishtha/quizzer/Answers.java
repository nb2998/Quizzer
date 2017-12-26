package com.apps.nishtha.quizzer;

import java.io.Serializable;

/**
 * Created by nishtha on 24/12/17.
 */

class Answers implements Serializable{
    private String correct;
    private String text;

    public String getCorrect() {
        return correct;
    }

    public String getText() {
        return text;
    }
}
