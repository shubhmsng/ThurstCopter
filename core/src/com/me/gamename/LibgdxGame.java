package com.me.gamename;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.awt.event.HierarchyBoundsAdapter;

import javax.swing.text.View;

public class LibgdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img, plane, gameOver;
    OrthographicCamera camera;
    TextureRegion terrainAbove, terrainBelow, pillarUp, pillarDown, gem, rock;
    float terrainOffset = 0;
    Vector2 planePosition= new Vector2();
    Vector2 planeDefaultPosition= new Vector2();
    Vector2 touchPosition=new Vector2();
    Vector2 touchPosition2 = new Vector2();
    Vector2 pillarPosition = new Vector2();
    Vector2 pillarPosition2 = new Vector2();
    Vector2 gemPosition = new Vector2();
    Vector2 rockPosition= new Vector2();
    Music bg_music, game_over;
    BitmapFont font,score, restart, close ;



    private int Score = 0,high_score=0;

    static enum GameState
    {
        INIT, ACTION, GAME_OVER
    }
    GameState gameState = GameState.INIT;

    @Override
	public void create () {
		batch = new SpriteBatch();
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(30, 30*(h/w));
        camera.setToOrtho(false, 800, 400);
        img = new Texture("sky1.png");
        terrainAbove=new TextureRegion(new Texture("groungGrass.png"));
        terrainBelow=new TextureRegion(terrainAbove);
        terrainBelow.flip(true, true);
        plane = new Texture("helicopter2.gif");
        gameOver = new Texture("GameOver.png");
        pillarUp = new TextureRegion((new Texture("piller1.png")));
        pillarDown = new TextureRegion(pillarUp);
        rock = new TextureRegion(new Texture("rock.png"));
        gem = new TextureRegion(new Texture("gem.png"));

        bg_music = Gdx.audio.newMusic(Gdx.files.internal("BackGroundMusic .mp3"));
        bg_music.setLooping(true);
        game_over = Gdx.audio.newMusic(Gdx.files.internal("crash.wav"));
        bg_music.play();

        font = new BitmapFont(Gdx.files.internal("verdana39.fnt"), Gdx.files.internal("verdana39.png"), false);
        font.setColor(Color.ROYAL);
        score = new BitmapFont(Gdx.files.internal("verdana39.fnt"), Gdx.files.internal("verdana39.png"), false);
        score.setColor(Color.GREEN);
        restart = new BitmapFont(Gdx.files.internal("verdana39.fnt"), Gdx.files.internal("verdana39.png"), false);
        restart.setColor(Color.CHARTREUSE);
        close = new BitmapFont(Gdx.files.internal("verdana39.fnt"), Gdx.files.internal("verdana39.png"), false);
        close.setColor(Color.CHARTREUSE);

        resetScene();
	}

	@Override
    public void render() {
        updateScene();
        drawScene();
	}

    public void updateScene(){
        float deltaTime = Gdx.graphics.getDeltaTime();

        if(Gdx.input.justTouched()){
            touchPosition.set(Gdx.input.getX(), Gdx.input.getY());
            if(gameState == GameState.INIT) {
                    gameState = GameState.ACTION;
                    return;
            }
            if(gameState == GameState.GAME_OVER) {

                    gameState = GameState.INIT;
                    resetScene();
                    return;
            }
                /*
                touchPosition.set(Gdx.input.getX(), Gdx.input.getY());
                if(touchPosition.x < 250)
                {

                    planePosition.y += 950*deltaTime;
                }
                else {
                    planePosition.y -= 950*deltaTime;
                }
                */
            if(gameState == GameState.INIT || gameState == GameState.GAME_OVER) {
                    return;
            }
        }
        if(gameState != GameState.GAME_OVER && Gdx.input.isTouched()){
            touchPosition2.set(Gdx.input.getX(), Gdx.input.getY());
            planePosition.y = 480 - touchPosition2.y;
        }
        terrainOffset-=300*deltaTime;
        pillarPosition.x -= 300*deltaTime;
        pillarPosition2.x -= 300*deltaTime;
        gemPosition.x -= 300*deltaTime;
        rockPosition.x -= 300*deltaTime;
        if(terrainOffset < -800)
        {
            terrainOffset = 0;
        }
        if(pillarPosition.x < 0){
            pillarPosition.x = (float)(800+ Math.random()*1500);
            pillarPosition.y = (float)(230 + (130/(Math.random()*50)));
        }
        if(pillarPosition2.x < 0){
            pillarPosition2.x = (float)(900+Math.random()*2500);
            pillarPosition2.y = (float)(50 - (130/(Math.random()*50)));
        }
        if(Math.abs(planePosition.x - gemPosition.x) < 130 && Math.abs(planePosition.y - gemPosition.y) < 40){
                gemPosition.x = (float)(950+Math.random()*1000);
                gemPosition.y = (float) (100 + Math.random()*(100));
                Score += 100;
        }
        if(gemPosition.x < 0){
            gemPosition.x = (float)(950+Math.random()*1000);
            gemPosition.y = (float) (100 + Math.random()*(100));
        }
        if(rockPosition.x < -(Math.random()*3000)){
            if(pillarPosition2.x >= pillarPosition.x){
                rockPosition.x = (float)(pillarPosition2.x + Math.random()*1030);
            }
            else{
                rockPosition.x = (float)(pillarPosition.x + Math.random()*1030);
            }
        }
        if(Math.abs(pillarPosition.x - planePosition.x) < 45 && Math.abs(pillarPosition.y - planePosition.y) < 50){
            if(gameState != GameState.GAME_OVER)
            {
                gameState = GameState.GAME_OVER;

            }
        }
        if(Math.abs(pillarPosition2.x - planePosition.x) < 45 && Math.abs(pillarPosition2.y - planePosition.y) < 110){
            if(gameState != GameState.GAME_OVER)
            {
                gameState = GameState.GAME_OVER;

            }
        }
        if(Math.abs(rockPosition.x - planePosition.x) < 60 &&  Math.abs(planePosition.y - rockPosition.y) < 40){
            if(gameState != GameState.GAME_OVER)
            {
                gameState = GameState.GAME_OVER;

            }
        }
        if(planePosition.y < 50)
        {
            if(gameState != GameState.GAME_OVER)
            {
                gameState = GameState.GAME_OVER;

            }
        }
        if(planePosition.y > 290){
            if(gameState != GameState.GAME_OVER)
            {
                gameState = GameState.GAME_OVER;

            }
        }
        if(Score > high_score){
            high_score = Score;
        }

        if(gameState == GameState.GAME_OVER){
            bg_music.stop();
            game_over.play();
            game_over.setLooping(false);
        }
        if(gameState != GameState.GAME_OVER){
            game_over.stop();
            bg_music.play();
        }

    }

    public void drawScene(){
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(img, terrainOffset, 0);
        batch.draw(pillarUp, pillarPosition.x, pillarPosition.y);
        batch.draw(pillarDown, pillarPosition2.x, pillarPosition2.y);
        batch.draw(terrainBelow, terrainOffset, 0);
        batch.draw(terrainBelow, terrainOffset + terrainBelow.
                getRegionWidth(), 0);
        batch.draw(terrainAbove, terrainOffset, 400 - terrainAbove.
                getRegionHeight());
        batch.draw(terrainAbove, terrainOffset + terrainAbove.
                getRegionWidth(), 400 - terrainAbove.getRegionHeight());
        batch.draw(plane, planePosition.x, planePosition.y);
        batch.draw(gem, gemPosition.x, gemPosition.y);
        batch.draw(rock, rockPosition.x, rockPosition.y);
        if(gameState == GameState.GAME_OVER) {
            resetScene();
            batch.draw(gameOver, 250, 150);
            score.draw(batch, "Score :" + Integer.toString(high_score), 20, 120);
            font.draw(batch,"High Score :"+Integer.toString(high_score), 350, 120);
        }
        if(gameState != GameState.GAME_OVER) {

            score.draw(batch, "Score :" + Integer.toString(Score), 20, 400);
            font.draw(batch, "High Score :" + Integer.toString(high_score), 350, 400);
        }
        batch.end();
    }

    private void resetScene()
    {
            terrainOffset=0;
            Score = 0;
            pillarPosition.set(850, 230);
            pillarPosition2.set(1020, 50);
            rockPosition.set(4632, 150);
            gemPosition.set(910, 120);
            planeDefaultPosition.set(100, 200);
            planePosition.set(planeDefaultPosition.x, planeDefaultPosition.y);
    }
}
