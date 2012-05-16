package com.wordsbattle;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.modifier.ScaleModifier;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.modifier.ease.EaseBounceOut;

import android.view.Display;
import android.view.Gravity;
import android.widget.Toast;

public class WordsBattleActivity extends BaseGameActivity {
    /*
     * Положение спрайтов задается кординатой левого верхнего угла спрайта.
     * В сетке хранятся координаты центров спрайтов.
     */
    private static int CAMERA_WIDTH;
    private static int CAMERA_HEIGHT;
    public static final int SPRITE_SIZE = 128;
    public static final float SCALE = 0.55f;
    
    // Смещения от краев экрана.
    public static final float leftOffset = 20;
    public static final float rightOffset = 180;
    
    public static final float upOffset = SPRITE_SIZE * SCALE + 5;
    public static final float downOffset = SPRITE_SIZE * SCALE + 5;
    
    public static boolean letterIsPressed;
    public static Letter pressedLetter;
    
    public static float pressedLetterX;
    public static float pressedLetterY;

    private Camera mCamera;
    
    private TexturesBase texBase;
    
    public static Word myWord;
    
    @Override
    public Engine onLoadEngine() {
        Display display = getWindowManager().getDefaultDisplay();
        CAMERA_WIDTH = display.getWidth();
        CAMERA_HEIGHT = display.getHeight();
        this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera));
    }

    @Override
    public void onLoadResources() {
        // TODO(acbelter): Сделать фон из текстуры.
        texBase = new TexturesBase(this, this.mEngine);
    }

    @Override
    public Scene onLoadScene() {
        this.mEngine.registerUpdateHandler(new FPSLogger());
        
        // TODO(acbelter): Был глюк,что буквы сами появляются на сетке.
        // TODO(acbelter): После разблокировки экрана иногда не работает тачскрин и сцена перезагружается.
        // TODO(acbelter): Причем не работают нажатия только на кнопки в сетке.
        // TODO(acbelter): Иногда неправильное отображение слов.
        final CoordinateGrid fieldGrid = new CoordinateGrid(CAMERA_WIDTH, CAMERA_HEIGHT, SPRITE_SIZE, SCALE);

        final Scene scene = new Scene();
        scene.setBackground(new ColorBackground(10f/255f, 134f/255f, 7f/255f));
        // scene.setBackground(new SpriteBackground(new Sprite(0, 0, mWordTextureRegion)));

        for (int i = 0; i < fieldGrid.getGrid().size(); i++) { 
            Pair<Float, Float> point = fieldGrid.getGrid().get(i);
            char newLetterChar = LetterGenerator.generateLetter();
                 
            final Letter newLetter = new Letter(newLetterChar, 
                                                point.getKey() - SPRITE_SIZE * SCALE / 2, 
                                                point.getValue() - SPRITE_SIZE * SCALE / 2,
                                                texBase);
            newLetter.setScale(0);
            newLetter.registerEntityModifier(new ScaleModifier(1.5f, 0, SCALE, EaseBounceOut.getInstance()));
            
            scene.attachChild(newLetter, 0);
            scene.registerTouchArea(newLetter);
        }

        float fieldWidth = CAMERA_WIDTH - leftOffset - rightOffset;
        int wordMaxLength = (int) (fieldWidth / (SPRITE_SIZE * SCALE));
        
        // Позиции, от которых рисуются слова.
        float wordX = leftOffset + (fieldWidth - wordMaxLength * SPRITE_SIZE * SCALE) / 2;
        
        float opponentWordY = (upOffset - SPRITE_SIZE * SCALE) / 2;
        float playerWordY = CAMERA_HEIGHT - SPRITE_SIZE * SCALE - (downOffset - SPRITE_SIZE * SCALE) / 2;
        
        final Word opponentWord = new Word(wordMaxLength, wordX, opponentWordY, texBase.getPlaceTexture(), false);
        final Word playerWord = new Word(wordMaxLength, wordX, playerWordY, texBase.getPlaceTexture(), true);
        
        myWord = playerWord;
        
        for (Sprite spr : opponentWord.cells) {
            scene.attachChild(spr, 0);
            scene.registerTouchArea(spr);
        }
        
        for (Sprite spr : playerWord.cells) {
            scene.attachChild(spr, 0);
            scene.registerTouchArea(spr);
        }

        Sprite menuButton = new Sprite(CAMERA_WIDTH - 2 * SPRITE_SIZE + (SPRITE_SIZE - SPRITE_SIZE * SCALE),
                                       -(SPRITE_SIZE - SPRITE_SIZE * SCALE) * 0.5f,
                                       texBase.getMenuButtonTexture()) {
            @Override
            public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
                // TODO(acbelter): Реализовать этот метод.
                return true;
            }
        };


        menuButton.setScale(0);
        menuButton.registerEntityModifier(new ScaleModifier(3, 0, SCALE, EaseBounceOut.getInstance()));

        scene.attachChild(menuButton);
        scene.registerTouchArea(menuButton);

        Sprite submitButton = new Sprite(CAMERA_WIDTH - 2 * SPRITE_SIZE + (SPRITE_SIZE - SPRITE_SIZE * SCALE),
                                         CAMERA_HEIGHT - SPRITE_SIZE * SCALE - (SPRITE_SIZE - SPRITE_SIZE * SCALE) * 0.5f,
                                         texBase.getSubmitButtonTexture()) {
            @Override
            public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
                    Toast toast = Toast.makeText(getApplicationContext(), playerWord.getWord(), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                    return true;
                }
            };
            

        submitButton.setScale(0);
        submitButton.registerEntityModifier(new ScaleModifier(3, 0, SCALE, EaseBounceOut.getInstance()));
        
        scene.attachChild(submitButton);
        scene.registerTouchArea(submitButton);
            
        scene.setTouchAreaBindingEnabled(true);
        return scene;
    }

    @Override
    public void onLoadComplete() {

    }
}