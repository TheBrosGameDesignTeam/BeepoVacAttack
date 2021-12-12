package BeepoVacAttack.GamePlay;

import BeepoVacAttack.BeepoVacClient.MainGame;
import Tweeninator.Tween;
import Tweeninator.TweenEasings;
import Tweeninator.TweenManager;
import Tweeninator.TweenSequence;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class GameOverScreen
{
    private float dimOpacity = 0f;
    private float gameOverLabelY = -200;
    private float percentCleanLabelY = MainGame.getHeight() + 200;

    public Tween tweenBackgroundDim() { return new Tween(() -> dimOpacity, (a) -> dimOpacity = (float)a); }
    public Tween tweenGameOverLabelY() { return new Tween(() -> gameOverLabelY, (a) -> gameOverLabelY = (float)a); }
    public Tween tweenPercentCleanLabelY() { return new Tween(() -> percentCleanLabelY, (a) -> percentCleanLabelY = (float)a); }

    private final String gameOverString = "Game Over!";
    private final String youGotStringTemplate = "The House is [P]% Clean!";

    private float percentClean = 0f;

    public void animateIn(float percentClean)
    {
        this.percentClean = percentClean;
        TweenSequence seq = new TweenSequence()
                .then(tweenBackgroundDim().to(0.7f).withDuration(200))
                .and(tweenGameOverLabelY().to(MainGame.getHeight() * 0.4f).withDuration(800).withEase(TweenEasings.OutBack()))
                .then(tweenPercentCleanLabelY().to(MainGame.getHeight() * 0.5f).withDuration(800).withEase(TweenEasings.OutBack()))
                ;
        TweenManager.add(seq);
    }
    public void render(Graphics g)
    {
        g.setColor(new Color(0.0f, 0.0f, 0.0f, dimOpacity));
        g.fillRect(0, 0, MainGame.getWidth(), MainGame.getHeight());

        g.setColor(Color.white);
        g.setFont(MainGame.getLargeFont());
        float gameOverLabelX = MainGame.xPosForStringCenteredAt(
                MainGame.getWidth() / 2,
                gameOverString,
                MainGame.getLargeFont()
        );
        g.drawString(gameOverString, gameOverLabelX, gameOverLabelY);

        String youGotString = youGotStringTemplate.replace("[P]", String.format("%.0f", 100 - percentClean));
        float percentCleanLabelX = MainGame.xPosForStringCenteredAt(
                MainGame.getWidth() / 2,
                youGotString,
                MainGame.getLargeFont()
        );
        g.drawString(youGotString, percentCleanLabelX, percentCleanLabelY);
    }
}
