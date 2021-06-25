package animations;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class Grow {
    private ScaleTransition scaleTransition;

    public Grow(Node node){
        scaleTransition = new ScaleTransition(Duration.seconds(2), node);
        scaleTransition.setFromX(0f);
        scaleTransition.setToX(1);
        scaleTransition.setFromY(0f);
        scaleTransition.setToY(1);
    }

    public void playAnimation(){
        scaleTransition.playFromStart();
    }
}
