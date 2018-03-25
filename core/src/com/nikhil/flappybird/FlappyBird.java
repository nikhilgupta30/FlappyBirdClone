package com.nikhil.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture backGround;
	ShapeRenderer shapeRenderer;

	Texture gameOver;

	Texture[] birds;
	int flapState=0;
	int count=0;
	float birdY = 0;
	float velocity =0;
	Circle birdCircle;

	int score = 0;
	int scoringTube = 0;
	BitmapFont fontScore;

	int gameState =0;
	float gravity = 1;
	int gap = 400;

	Texture topTube;
	Texture bottomTube;
	float maxTubeOffset;

	Random randomGenerator;

	int numberOfTubes = 4;
	float distanceBwTube;
	float tubeVelocity = 7;
	float[] tubeX = new float[numberOfTubes];
	float[] tubeOffset = new float[numberOfTubes];
	Rectangle[] topTubeRectangle;
	Rectangle[] bottomTubeRectangle;
	
	@Override
	public void create () {
		gameOver = new Texture("gameover.png");

		shapeRenderer = new ShapeRenderer();
		birdCircle = new Circle();
		fontScore = new BitmapFont();
		fontScore.setColor(Color.WHITE);
		fontScore.getData().setScale(5);

		batch = new SpriteBatch();
		backGround = new Texture("bg.png");

		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");

		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
		maxTubeOffset = Gdx.graphics.getHeight()/2 - gap/2 - 100;

		randomGenerator = new Random();
		distanceBwTube = Gdx.graphics.getWidth()*2/3;

		topTubeRectangle = new Rectangle[numberOfTubes];
		bottomTubeRectangle = new Rectangle[numberOfTubes];

		startGame();
	}

	public void startGame(){
		birdY = Gdx.graphics.getHeight()/2- birds[flapState].getHeight()/2;

		for(int i=0;i<numberOfTubes;i++){

			tubeOffset[i] = (randomGenerator.nextFloat()-0.5f)* (Gdx.graphics.getHeight()-gap-200);

			tubeX[i] = 2*Gdx.graphics.getWidth()-topTube.getWidth()/2 + i*distanceBwTube;

			topTubeRectangle[i] = new Rectangle();
			bottomTubeRectangle[i] = new Rectangle();
		}

	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(backGround,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		if(gameState ==1){

			if(count>4){
				flapState = 1;
			}
			else{
				flapState = 0;
			}

			count = (count+1)%10;

			if (tubeX[scoringTube] < Gdx.graphics.getWidth()/2){
				score++;
				//Gdx.app.log("Score",score+"");
				scoringTube = (scoringTube+1)%numberOfTubes;
			}

			if(Gdx.input.justTouched()){

				if(birdY < Gdx.graphics.getHeight()-birds[flapState].getHeight()){
					velocity = -21;
				}

			}

			for(int i=0;i<numberOfTubes;i++){

				if(tubeX[i] < -topTube.getWidth()){
					tubeX[i] += numberOfTubes*distanceBwTube;
					tubeOffset[i] = (randomGenerator.nextFloat()-0.5f)*
							(Gdx.graphics.getHeight()-gap-200);

				}
				else{
					tubeX[i] -= tubeVelocity;
				}

			}


			if(birdY > 0){
				velocity += gravity;
				birdY -= velocity;
			}
			else{
				gameState = 2;
			}

			fontScore.draw(batch,String.valueOf(score),50,100);

		}
		else if(gameState == 0){

			if(count>4){
				flapState = 1;
			}
			else{
				flapState = 0;
			}

			count = (count+1)%10;

			if(Gdx.input.justTouched()){

				gameState =1;

			}
		}
		else{

			if(Gdx.input.justTouched()){
				gameState =0;
				startGame();
				score = 0;
				scoringTube = 0;
				velocity = 0;
				fontScore.getData().setScale(5);
			}
		}


		for(int i=0;i<numberOfTubes;i++){
			batch.draw(topTube,tubeX[i], Gdx.graphics.getHeight()/2 + gap/2 + tubeOffset[i]);
			batch.draw(bottomTube,tubeX[i], Gdx.graphics.getHeight()/2 - gap/2 -
					bottomTube.getHeight() + tubeOffset[i]);

			topTubeRectangle[i] = new Rectangle(tubeX[i],
					Gdx.graphics.getHeight()/2 + gap/2 + tubeOffset[i],
					topTube.getWidth(),topTube.getHeight());
			bottomTubeRectangle[i] = new Rectangle(tubeX[i],
					Gdx.graphics.getHeight()/2 -gap/2 -bottomTube.getHeight() +tubeOffset[i],
					bottomTube.getWidth(),bottomTube.getHeight());
		}

		batch.draw(birds[flapState],Gdx.graphics.getWidth()/2- birds[flapState].getWidth()/2,birdY);



		if(gameState==2){
			batch.draw(gameOver,Gdx.graphics.getWidth()/2-gameOver.getWidth()/2,
					Gdx.graphics.getHeight()*3/5);
			fontScore.getData().setScale(10);
			fontScore.draw(batch,String.valueOf(score),
					Gdx.graphics.getWidth()/2-50,
					Gdx.graphics.getHeight()/2-50);
		}

		batch.end();

		birdCircle.set(Gdx.graphics.getWidth()/2,birdY+birds[flapState].getHeight()/2,
				birds[flapState].getHeight()-50);

//		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//		shapeRenderer.setColor(Color.RED);
//		shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);

		for (int i=0;i<numberOfTubes;i++){
//			shapeRenderer.rect(tubeX[i],
//					Gdx.graphics.getHeight()/2 + gap/2 + tubeOffset[i],
//					topTube.getWidth(),topTube.getHeight());
//			shapeRenderer.rect(tubeX[i],
//					Gdx.graphics.getHeight()/2 -gap/2 -bottomTube.getHeight() +tubeOffset[i],
//					bottomTube.getWidth(),bottomTube.getHeight());

			if(Intersector.overlaps(birdCircle,topTubeRectangle[i]) ||
					Intersector.overlaps(birdCircle,bottomTubeRectangle[i])){
				gameState = 2;
			}
		}

		shapeRenderer.end();

	}

}
