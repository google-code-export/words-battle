package com.wordsbattle;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.modifier.ScaleModifier;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.SpriteBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.modifier.ease.EaseBounceOut;

import com.wordsbattle.common.domain.LetterPool;
import com.wordsbattle.net.*;
import com.wordsbattle.scene.MenuScene;
import com.wordsbattle.scene.SceneManager;
import com.wordsbattle.util.LetterGenerator;
import com.wordsbattle.util.Pair;
import com.wordsbattle.util.TexturesBase;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
    
    // TODO: Сделать подержку всех разрешений автоматически: см. drawable-hdpi.
    public static final float upOffset = SPRITE_SIZE * SCALE + 5;
    public static final float downOffset = SPRITE_SIZE * SCALE + 5;
    
    public static boolean letterIsPressed;
    public static Letter pressedLetter;
    
    public static float pressedLetterX;
    public static float pressedLetterY;
    
    public static Scene gameScene;

    private Camera mCamera;
    
    // TODO(acbelter): Удалить texBase из параметров методов.
    public static TexturesBase texBase;
    
    public static Word myWord;
    
    public static CoordinateGrid fieldGrid;
    
    public static Engine engine;
    
    @Override
    public Engine onLoadEngine() {
        Display display = getWindowManager().getDefaultDisplay();
        CAMERA_WIDTH = display.getWidth();
        CAMERA_HEIGHT = display.getHeight();
        this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);   
        engine = new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera));
        return engine;
    }

    @Override
    public void onLoadResources() {
        texBase = new TexturesBase(this, this.mEngine);
    }

    @Override
    public Scene onLoadScene() {
        this.mEngine.registerUpdateHandler(new FPSLogger());

        String host = "192.168.123.1";
        int port = 6789;
        
        WBClient firstClient = new WBClient(host, port, new IWBClientDelegate() {

            @Override
            public void UserNameAlreadyExists() {}

            @Override
            public void UserTriesToReregister() {}

            @Override
            public void UserNameSuccessfullyRegistered() {}

            @Override
            public boolean opponentRequestsGame(String opponentName) {return false;}

            @Override
            public void opponentAcceptedGameRequest(String opponentName) {}

            @Override
            public void opponentDeniedGameRequest(String opponentName) {}

            @Override
            public void gameStarted() {}

            @Override
            public void gamePaused() {}

            @Override
            public void gameResumed() {}

            @Override
            public void gameEnded() {}

            @Override
            public void newPool(LetterPool pool) {}
            
            });
        
    if (firstClient.ConnectToServer()) {
            firstClient.sendRegitsteringRequestForPlayerName("bla"); // register successfully
            firstClient.sendRegitsteringRequestForPlayerName("bla"); // Reregister fault
            try {
                    Thread.sleep(1000);
            } catch (InterruptedException e) {
                    e.printStackTrace();
            }                       
    } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Can't connect to server")
                   .setCancelable(false)
                   .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                           WordsBattleActivity.this.finish();
                       }
                   })
                   .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                       }
                   });
            AlertDialog alert = builder.create();
            alert.show();
    }

        // TODO(acbelter): Был глюк,что буквы сами появляются на сетке.
        // TODO(acbelter): После разблокировки экрана иногда не работает тачскрин и сцена перезагружается.
        // TODO(acbelter): Причем не работают нажатия только на кнопки в сетке.
        // TODO(acbelter): Иногда неправильное отображение слов.
        // TODO(acbelter): Новые буквы не привязываются к сцене.
        fieldGrid = new CoordinateGrid(CAMERA_WIDTH, CAMERA_HEIGHT, SPRITE_SIZE, SCALE);

        SceneManager.init(this);
        MenuScene.load();
        
        //gameScene.setBackground(new ColorBackground(10f/255f, 134f/255f, 7f/255f));
        gameScene = new Scene();
        gameScene.setBackground(new SpriteBackground(new Sprite(0, 0, texBase.getBackgroundTexture())));

        for (int i = 0; i < fieldGrid.getGrid().size(); i++) { 
            Pair<Float, Float> point = fieldGrid.getGrid().get(i);
            //char newLetterChar = conn.getNewLetter();
            char newLetterChar = LetterGenerator.generateLetter();
                 
            final Letter newLetter = new Letter(newLetterChar, 
                                                point.getKey() - SPRITE_SIZE * SCALE / 2, 
                                                point.getValue() - SPRITE_SIZE * SCALE / 2,
                                                texBase);
            newLetter.setScale(0);
            newLetter.registerEntityModifier(new ScaleModifier(1.5f, 0, SCALE, EaseBounceOut.getInstance()));
            
            gameScene.attachChild(newLetter, 0);
            gameScene.registerTouchArea(newLetter);
            point.setPointLetter(newLetter);
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
            gameScene.attachChild(spr, 0);
            gameScene.registerTouchArea(spr);
        }
        
        for (Sprite spr : playerWord.cells) {
            gameScene.attachChild(spr, 0);
            gameScene.registerTouchArea(spr);
        }

        // TODO(acbelter): Разобраться с добавлением новых букв.
        Sprite menuButton = new Sprite(CAMERA_WIDTH - 2 * SPRITE_SIZE + (SPRITE_SIZE - SPRITE_SIZE * SCALE),
                                       -(SPRITE_SIZE - SPRITE_SIZE * SCALE) * 0.5f,
                                       texBase.getMenuButtonTexture()) {
            @Override
            public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
                // TODO(acbelter): Реализовать этот метод.
                // Пока тут будет тест сервера.
                //FieldFiller.fill(gameScene, conn);
                SceneManager.setScene(MenuScene.run());
                return false;
            }
        };


        menuButton.setScale(0);
        menuButton.registerEntityModifier(new ScaleModifier(3, 0, SCALE, EaseBounceOut.getInstance()));

        gameScene.attachChild(menuButton);
        gameScene.registerTouchArea(menuButton);

        Sprite submitButton = new Sprite(CAMERA_WIDTH - 2 * SPRITE_SIZE + (SPRITE_SIZE - SPRITE_SIZE * SCALE),
                                         CAMERA_HEIGHT - SPRITE_SIZE * SCALE - (SPRITE_SIZE - SPRITE_SIZE * SCALE) * 0.5f,
                                         texBase.getSubmitButtonTexture()) {
            @Override
            public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
                    //String str = conn.checkWord(playerWord.getWord());
                    String str = playerWord.getWord().toUpperCase();
                  
                    Toast toast = Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                
                    return false;
                }
            };
            

        submitButton.setScale(0);
        submitButton.registerEntityModifier(new ScaleModifier(3, 0, SCALE, EaseBounceOut.getInstance()));
        
        gameScene.attachChild(submitButton);
        gameScene.registerTouchArea(submitButton);
            
        // TODO: При нажатии на меню нажимается game scene.
        gameScene.setTouchAreaBindingEnabled(false);
        //return gameScene;
        return MenuScene.run();
    }

    @Override
    public void onLoadComplete() {
        
    }
}