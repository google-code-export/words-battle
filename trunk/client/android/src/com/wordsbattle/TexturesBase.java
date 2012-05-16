package com.wordsbattle;

import org.anddev.andengine.engine.Engine;

import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import static com.wordsbattle.WordsBattleActivity.SPRITE_SIZE;

public class TexturesBase { 
    private BitmapTextureAtlas mBitmapTextureAtlas;
    
    private TextureRegion A_TextureRegion;
    private TextureRegion B_TextureRegion;
    private TextureRegion C_TextureRegion;
    private TextureRegion D_TextureRegion;
    private TextureRegion E_TextureRegion;
    private TextureRegion F_TextureRegion;
    private TextureRegion G_TextureRegion;
    private TextureRegion H_TextureRegion;
    private TextureRegion I_TextureRegion;
    private TextureRegion J_TextureRegion;
    private TextureRegion K_TextureRegion;
    private TextureRegion L_TextureRegion;
    private TextureRegion M_TextureRegion;
    private TextureRegion N_TextureRegion;
    private TextureRegion O_TextureRegion;
    private TextureRegion P_TextureRegion;
    private TextureRegion Q_TextureRegion;
    private TextureRegion R_TextureRegion;
    private TextureRegion S_TextureRegion;
    private TextureRegion T_TextureRegion;
    private TextureRegion U_TextureRegion;
    private TextureRegion V_TextureRegion;
    private TextureRegion W_TextureRegion;
    private TextureRegion X_TextureRegion;
    private TextureRegion Y_TextureRegion;
    private TextureRegion Z_TextureRegion;
    
    private TextureRegion mSubmitButtonTexture;
    private TextureRegion mMenuButtonTexture;
    
    // TODO(acbelter): Убрать полоски между клетками.
    // TODO(acbelter): Текстуры букв не центрированы!
    private TextureRegion mPlaceTextureRegion;
    
    public TexturesBase(BaseGameActivity activity, Engine engine) {
        this.mBitmapTextureAtlas = new BitmapTextureAtlas(1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        
        BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, activity, "texture.png", 0, 0);
        
        this.A_TextureRegion = TextureRegionFactory.extractFromTexture(mBitmapTextureAtlas, 0 * SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE, true);
        this.B_TextureRegion = TextureRegionFactory.extractFromTexture(mBitmapTextureAtlas, 1 * SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE, true);
        this.C_TextureRegion = TextureRegionFactory.extractFromTexture(mBitmapTextureAtlas, 2 * SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE, true);
        this.D_TextureRegion = TextureRegionFactory.extractFromTexture(mBitmapTextureAtlas, 3 * SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE, true);
        this.E_TextureRegion = TextureRegionFactory.extractFromTexture(mBitmapTextureAtlas, 4 * SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE, true);
        this.F_TextureRegion = TextureRegionFactory.extractFromTexture(mBitmapTextureAtlas, 5 * SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE, true);
        this.G_TextureRegion = TextureRegionFactory.extractFromTexture(mBitmapTextureAtlas, 6 * SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE, true);
        this.H_TextureRegion = TextureRegionFactory.extractFromTexture(mBitmapTextureAtlas, 7 * SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE, true);
        
        this.I_TextureRegion = TextureRegionFactory.extractFromTexture(mBitmapTextureAtlas, 0 * SPRITE_SIZE, 1 * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE, true);
        this.J_TextureRegion = TextureRegionFactory.extractFromTexture(mBitmapTextureAtlas, 1 * SPRITE_SIZE, 1 * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE, true);
        this.K_TextureRegion = TextureRegionFactory.extractFromTexture(mBitmapTextureAtlas, 2 * SPRITE_SIZE, 1 * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE, true);
        this.L_TextureRegion = TextureRegionFactory.extractFromTexture(mBitmapTextureAtlas, 3 * SPRITE_SIZE, 1 * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE, true);
        this.M_TextureRegion = TextureRegionFactory.extractFromTexture(mBitmapTextureAtlas, 4 * SPRITE_SIZE, 1 * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE, true);
        this.N_TextureRegion = TextureRegionFactory.extractFromTexture(mBitmapTextureAtlas, 5 * SPRITE_SIZE, 1 * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE, true);
        this.O_TextureRegion = TextureRegionFactory.extractFromTexture(mBitmapTextureAtlas, 6 * SPRITE_SIZE, 1 * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE, true);
        this.P_TextureRegion = TextureRegionFactory.extractFromTexture(mBitmapTextureAtlas, 7 * SPRITE_SIZE, 1 * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE, true);
        
        this.Q_TextureRegion = TextureRegionFactory.extractFromTexture(mBitmapTextureAtlas, 0 * SPRITE_SIZE, 2 * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE, true);
        this.R_TextureRegion = TextureRegionFactory.extractFromTexture(mBitmapTextureAtlas, 1 * SPRITE_SIZE, 2 * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE, true);
        this.S_TextureRegion = TextureRegionFactory.extractFromTexture(mBitmapTextureAtlas, 2 * SPRITE_SIZE, 2 * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE, true);
        this.T_TextureRegion = TextureRegionFactory.extractFromTexture(mBitmapTextureAtlas, 3 * SPRITE_SIZE, 2 * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE, true);
        this.U_TextureRegion = TextureRegionFactory.extractFromTexture(mBitmapTextureAtlas, 4 * SPRITE_SIZE, 2 * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE, true);
        this.V_TextureRegion = TextureRegionFactory.extractFromTexture(mBitmapTextureAtlas, 5 * SPRITE_SIZE, 2 * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE, true);
        this.W_TextureRegion = TextureRegionFactory.extractFromTexture(mBitmapTextureAtlas, 6 * SPRITE_SIZE, 2 * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE, true);
        this.X_TextureRegion = TextureRegionFactory.extractFromTexture(mBitmapTextureAtlas, 7 * SPRITE_SIZE, 2 * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE, true);
        
        this.Y_TextureRegion = TextureRegionFactory.extractFromTexture(mBitmapTextureAtlas, 0 * SPRITE_SIZE, 3 * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE, true);
        this.Z_TextureRegion = TextureRegionFactory.extractFromTexture(mBitmapTextureAtlas, 1 * SPRITE_SIZE, 3 * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE, true);
        
        this.mSubmitButtonTexture = TextureRegionFactory.extractFromTexture(mBitmapTextureAtlas, 2 * SPRITE_SIZE, 3 * SPRITE_SIZE, 2 * SPRITE_SIZE, SPRITE_SIZE, true);
        this.mMenuButtonTexture = TextureRegionFactory.extractFromTexture(mBitmapTextureAtlas, 4 * SPRITE_SIZE, 3 * SPRITE_SIZE, 2 * SPRITE_SIZE, SPRITE_SIZE, true);
        
        this.mPlaceTextureRegion = TextureRegionFactory.extractFromTexture(mBitmapTextureAtlas, 6 * SPRITE_SIZE, 3 * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE, true);

        engine.getTextureManager().loadTexture(this.mBitmapTextureAtlas);
    }
    
    public TextureRegion getCharTexture(char letterChar) {
        switch(letterChar) {
            case 'a': {
                return A_TextureRegion;
            }
            case 'b': {
                return B_TextureRegion;
            }
            case 'c': {
                return C_TextureRegion;
            }
            case 'd': {
                return D_TextureRegion;
            }
            case 'e': {
                return E_TextureRegion;
            }
            case 'f': {
                return F_TextureRegion;
            }
            case 'g': {
                return G_TextureRegion;
            }
            case 'h': {
                return H_TextureRegion;
            }
            case 'i': {
                return I_TextureRegion;
            }
            case 'j': {
                return J_TextureRegion;
            }
            case 'k': {
                return K_TextureRegion;
            }
            case 'l': {
                return L_TextureRegion;
            }
            case 'm': {
                return M_TextureRegion;
            }
            case 'n': {
                return N_TextureRegion;
            }
            case 'o': {
                return O_TextureRegion;
            }
            case 'p': {
                return P_TextureRegion;
            }
            case 'q': {
                return Q_TextureRegion;
            }
            case 'r': {
                return R_TextureRegion;
            }
            case 's': {
                return S_TextureRegion;
            }
            case 't': {
                return T_TextureRegion;
            }
            case 'u': {
                return U_TextureRegion;
            }
            case 'v': {
                return V_TextureRegion;
            }
            case 'w': {
                return W_TextureRegion;
            }
            case 'x': {
                return X_TextureRegion;
            }
            case 'y': {
                return Y_TextureRegion;
            }
            case 'z': {
                return Z_TextureRegion;
            }
            default : {
                return null;
            }
        }
    }

    public TextureRegion getPlaceTexture() {
        return this.mPlaceTextureRegion;
    }
    
    public TextureRegion getSubmitButtonTexture() {
        return this.mSubmitButtonTexture;
    }
    
    public TextureRegion getMenuButtonTexture() {
        return this.mMenuButtonTexture;
    }
}