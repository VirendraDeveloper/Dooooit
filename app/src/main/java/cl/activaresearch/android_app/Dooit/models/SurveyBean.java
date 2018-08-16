package cl.activaresearch.android_app.Dooit.models;

import java.io.Serializable;
import java.util.List;

/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 03 Jul,2018
 */
public class SurveyBean implements Serializable {

    /**
     * question : Test Texto Radio
     * type : 3
     * cat : S1
     * id : P3
     * studyId : 180002
     * questionnaireId : 861962
     * taskId : 255480
     * alternatives : [{"id":235129,"text":"Si","cod":"1","answered":true},{"id":235130,"text":"No","cod":"2","answered":false}]
     * answer : {"date":"2018-07-12","data":{"alternative":235129}}
     */

    private String question;
    private TaskQuestionType type;
    private String cat;
    private String id;
    private String studyId;
    private String questionnaireId;
    private String taskId;
    private AnswerBean answer;
    private boolean isAnswered;
    private List<AlternativesBean> alternatives;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public TaskQuestionType getType() {
        return type;
    }

    public void setType(TaskQuestionType type) {
        this.type = type;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudyId() {
        return studyId;
    }

    public void setStudyId(String studyId) {
        this.studyId = studyId;
    }

    public String getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(String questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    public boolean isAnswered() {
        return isAnswered;
    }

    public void setAnswered(boolean answered) {
        isAnswered = answered;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public AnswerBean getAnswer() {
        return answer;
    }

    public void setAnswer(AnswerBean answer) {
        this.answer = answer;
    }

    public List<AlternativesBean> getAlternatives() {
        return alternatives;
    }

    public void setAlternatives(List<AlternativesBean> alternatives) {
        this.alternatives = alternatives;
    }

    public static class AnswerBean implements Serializable {
        /**
         * date : 2018-07-12
         * data : {"alternative":235129}
         */

        private String date;
        private List<Integer> dataBean;
        private String data;

        public String getData() {
            return data;
        }

        public void setDataBean(String data) {
            this.data = data;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public List<Integer> getDataBean() {
            return dataBean;
        }

        public void setDataBean(List<Integer> dataBean) {
            this.dataBean = dataBean;
        }

    }

    public static class AlternativesBean implements Serializable {
        /**
         * id : 235129
         * text : Si
         * cod : 1
         * answered : true
         */

        private int id;
        private String text;
        private String cod;
        private boolean answered;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getCod() {
            return cod;
        }

        public void setCod(String cod) {
            this.cod = cod;
        }

        public boolean isAnswered() {
            return answered;
        }

        public void setAnswered(boolean answered) {
            this.answered = answered;
        }
    }
}
