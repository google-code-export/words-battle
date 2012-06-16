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
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Display;
import android.view.Gravity;
import android.widget.Toast;

import com.wordsbattle.common.domain.Letter;
import com.wordsbattle.common.domain.LetterPool;
import com.wordsbattle.common.domain.Player;
import com.wordsbattle.net.IWBClientDelegate;
import com.wordsbattle.net.WBClient;
import com.wordsbattle.scene.MenuScene;
import com.wordsbattle.scene.SceneManager;
import com.wordsbattle.util.Pair;
import com.wordsbattle.util.TexturesBase;

public class WordsBattleActivity extends BaseGameActivity implements IWBClientDelegate {
    private final static Logger LOGGER = Logger.getLogger(WordsBattleActivity.class);
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
    public static LetterSprite pressedLetter;

    public static float pressedLetterX;
    public static float pressedLetterY;

    public static Scene gameScene;

    private static String host = "192.168.1.2";
    private static int port = 6789;

    public static WBClient client;
    private static final String testingOpponentName = "hunternif-opp"; 

    private Camera mCamera;

    // TODO(acbelter): Удалить texBase из параметров методов.
    public static TexturesBase texBase;

    public static WordSprite myWord;

    public static CoordinateGrid fieldGrid;

    public static Engine engine;

    @Override
    public Engine onLoadEngine() {
        if (!Logger.getRootLogger().getAllAppenders().hasMoreElements()) {
            Logger.getRootLogger().addAppender(new ConsoleAppender(new PatternLayout("%r [%t] %p %c %x {%M} - %m%n")));
        }
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

        client = new WBClient(host, port, this);

        if (client.ConnectToServer()) {
            client.sendRegitsteringRequestForPlayerName("acbelter");
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
        // TODO(acbelter):Попросить пользователя ввести имя [userName].
        // затем вызвать client.sendRegitsteringRequestForPlayerName(userName);
        // Далее все дело проиходит в вызове реализации IWBClientDelegate в методе UserNameSuccessfullyRegistered(), либо в любое время после.
        // тоесть, когда игрок зарегистрировался, дальше он уже может запрашивать игру. Можешь пока это делать hard кодом, а можешь выдавать окошки,
        // которые будут спрашивать у пользователя имя оппонента.

        //gameScene.setBackground(new ColorBackground(10f/255f, 134f/255f, 7f/255f));
        gameScene = new Scene();
        gameScene.setBackground(new SpriteBackground(new Sprite(0, 0, texBase.getBackgroundTexture())));



        float fieldWidth = CAMERA_WIDTH - leftOffset - rightOffset;
        int wordMaxLength = (int) (fieldWidth / (SPRITE_SIZE * SCALE));

        // Позиции, от которых рисуются слова.
        float wordX = leftOffset + (fieldWidth - wordMaxLength * SPRITE_SIZE * SCALE) / 2;

        float opponentWordY = (upOffset - SPRITE_SIZE * SCALE) / 2;
        float playerWordY = CAMERA_HEIGHT - SPRITE_SIZE * SCALE - (downOffset - SPRITE_SIZE * SCALE) / 2;

        final WordSprite opponentWord = new WordSprite(wordMaxLength, wordX, opponentWordY, texBase.getPlaceTexture(), false);
        final WordSprite playerWord = new WordSprite(wordMaxLength, wordX, playerWordY, texBase.getPlaceTexture(), true);

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

        //fillgrid
        
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

    /** Этот метод обновляет игровое поле в соответствии с пришедшим новым пулом <b>pool</b>
     * @param pool новый пул пришедший от сервера */    
    public void udateGrid(LetterPool pool) {
        // TODO:(danichbloomTOacbelter): сделать так чтобы размер fieldGrid совпадал с получаемым пулом.        
        for (int i = 0; i < fieldGrid.getGrid().size(); i++) {
            LOGGER.info("Point i: " + i);
            Pair<Float, Float> point = fieldGrid.getGrid().get(i);
            if (i < pool.size()) {
                Letter newLetter = pool.get(i);  // буква из нового пула
                LetterSprite localLetterSprite = point.getPointLetter();  // буква-спрайт из поля, которое на экране
                
                if (newLetter == null) {  // Если в новом пуле на месте i нет буквы...
                    LOGGER.debug("newLetter == null");
                    if (localLetterSprite != null) {  // Если на поле в этом месте есть буква-спрайт...
                        LOGGER.debug("localLetterSprite != null");
                        Letter localLetter = localLetterSprite.getLetter();  // извлекаем букву из спрайта
                        if (localLetter != null) {  // Если это оказывается не пустое место. (кстати сейчас иначе быть не может, мы удаляем саму букву-спрайт)
                            LOGGER.debug("deleteing localLetter: " + localLetter);
                            gameScene.detachChild(localLetterSprite);  // удаляем букву с поля, потому что пришла буква null
                            fieldGrid.deleteLetter(localLetterSprite);
                            //TODO(danichbloomTOacbelter): Do I remove the letter correctly? Some times when fake opponent picks a letter, 
                            // game fails with error java.lang.IndexOutOfBoundsException: Invalid index 34, size is 34.
                            // FATAL EXCEPTION: GLThread
                        }
                    }
                } else {  // Если в новом пуле на месте i есть буква...
                    LOGGER.debug("newLetter != null");
                    if (localLetterSprite != null) {  // Если на поле в этом месте есть буква-спрайт...
                        LOGGER.debug("localLetterSprite != null");                        
                        Letter localLetter = localLetterSprite.getLetter();  // извлекаем букву из спрайта
                        if (localLetter != null) {  // Если это оказывается не пустое место. (кстати сейчас иначе быть не может, мы удаляем саму букву-спрайт)
                            LOGGER.debug("localLetter != null");
                            if (newLetter.getId() != localLetter.getId()) {  // если новопришедная буква отличается от той что там должна стоять
                                // новая буква пришла на место старой, хотя старую никто не брал.
                                LOGGER.warn("Substitution of a letter in the grid without picking it!");
                            }
                            continue;
                        }
                    } else {  // Если на поле в этом месте нет буквы-спрайт...
                        LOGGER.debug("putting new letter: " + newLetter);
                        final LetterSprite newLetterSprite = new LetterSprite(newLetter, 
                                point.getKey() - SPRITE_SIZE * SCALE / 2, 
                                point.getValue() - SPRITE_SIZE * SCALE / 2,
                                texBase);

                        newLetterSprite.setScale(0);
                        newLetterSprite.registerEntityModifier(new ScaleModifier(1.5f, 0, SCALE, EaseBounceOut.getInstance()));

                        gameScene.attachChild(newLetterSprite, 0);
                        gameScene.registerTouchArea(newLetterSprite);
                        point.setPointLetter(newLetterSprite);
                        continue;  
                    }
                }                
            }
        }        
    }

    @Override
    public void onLoadComplete() {

    }

    @Override
    public void UserNameAlreadyExists() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void UserNameSuccessfullyRegistered(String registeredName) {
        // TODO(danichbloomTOacbelter): спросить у пользователя имя игрока с которым он хочет играть
        // Это пока это можно сделать сдесь, но вообще у пользователя, пока он меню, должна быть возможность
        // в любой момент после вызова UserNameSuccessfullyRegistered() сделать gameRequest. 
        // (тоесть посидеть подумать, а потом попросить игру)
        client.sendGameRequest(testingOpponentName);        
    }

    @Override
    public void UserTriesToReregister() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean opponentRequestsGame(String opponentName) {
        // TODO(acbelter):ask user if he want's to play with player with name "opponentName" and return answer
        return false;
    }

    @Override
    public void opponentAcceptedGameRequest(String opponentName) {
        // TODO(acbelter):в этот момент можно переходить к игровому полю.        
    }

    @Override
    public void opponentDeniedGameRequest(String opponentName) {
        // TODO(acbelter): дать пользователю возможность опять сделать gameRequest        
    }

    @Override
    public void gameStarted() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void gamePaused() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void gameResumed() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void gameEnded() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void update(LetterPool pool, Player player, Player opponent) {
        // TODO Auto-generated method stub
        udateGrid(pool); 
    }
}