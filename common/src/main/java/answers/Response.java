package answers;

import data.SpaceMarine;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Stack;

/**
 * Response form
 */
public class Response implements Serializable {
    private Stack<SpaceMarine> collection;
    private ResponseAnswer responseAnswer;
    private String responseBody;
    private String[] responseArgs;

    public Response(Stack<SpaceMarine> collection, ResponseAnswer responseAnswer, String responseBody, String[] responseArgs){
        this.collection = collection;
        this.responseAnswer = responseAnswer;
        this.responseBody = responseBody;
        this.responseArgs = responseArgs;
    }

    public ResponseAnswer getResponseAnswer() {
        return responseAnswer;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public String[] getResponseArgs() {
        return responseArgs;
    }

    public Stack<SpaceMarine> getCollection(){
        return this.collection;
    }

    @Override
    public String toString() {
        return "Ответ:\n" + "Код ответа: " + this.getResponseAnswer() + "\nТекст ответа: " + this.responseBody + "\n"
                + "Аргументы: " + Arrays.toString(this.getResponseArgs()) +
                "Коллекция: " + this.getCollection();
    }
}
