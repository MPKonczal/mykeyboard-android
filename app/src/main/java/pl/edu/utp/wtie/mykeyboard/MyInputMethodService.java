package pl.edu.utp.wtie.mykeyboard;

import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class MyInputMethodService extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    private KeyboardView keyboardView;
    private Keyboard keyboard;
    private Intent intent;
    private View view;
    private NfcAdapter nfcAdapter;

    @Override
    public View onCreateInputView() {
        keyboardView = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard_view, null);
        keyboard = new Keyboard(this, R.xml.content_pad_1);
        keyboardView.setKeyboard(keyboard);
        keyboardView.setOnKeyboardActionListener(this);
        return keyboardView;
    }

    public View onCreateSecondInputView() {
        keyboardView = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard_view, null);
        keyboard = new Keyboard(this, R.xml.content_pad_2);
        keyboardView.setKeyboard(keyboard);
        keyboardView.setOnKeyboardActionListener(this);
        return keyboardView;
    }

    @Override
    public void onPress(int i) {

    }

    @Override
    public void onRelease(int i) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection inputConnection = getCurrentInputConnection();

        if (inputConnection != null) {
            switch (primaryCode) {
                case 0:
                    inputConnection.commitText("Hello, I'm custom keyboard! ", 1);
                    break;
                case 1:
                    MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.f1_2018_theme_song);
                    mediaPlayer.start();
                    break;
                case 2:
                    startCamera();
                    break;
                case 3:
                    String fileName = "f1.txt";
                    String text = "The Brazilian Grand Prix 2019 was very exciting!";
                    saveToFile(fileName, text);
                    break;
                case 4:
                    displayToast();
                    break;
                case 5:
                    view = onCreateSecondInputView();
                    setInputView(view);
                    break;
                case 6:
                    checkNfcStatus();
                    break;
                case 7:
                    enableOrDisableNfc();
                    break;
                case 8:
                    openMailPage("https://mail.utp.edu.pl/mail/index.php");
                    break;
                case 9:
                    startYouTube();
                    break;
                case Keyboard.KEYCODE_CANCEL:
                    AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
                    audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
                    view = onCreateInputView();
                    setInputView(view);
                    break;
            }
        }
    }

    @Override
    public void onText(CharSequence charSequence) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

    private void startCamera() {
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void saveToFile(String fileName, String text) {
        try (FileOutputStream fileOutputStream = openFileOutput(fileName, MODE_PRIVATE)) {
            fileOutputStream.write(text.getBytes());
            Toast.makeText(this, "SAVED TO: " + getFilesDir() + "/" + fileName,
                    Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "File not found!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error saving to file!", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayToast() {
        final String[] TOAST = {"Never give up!", "Hello :)", "Take care :D",
                "I greet you!", "It will be fine.", "Something special will happen today ..."};
        Random random = new Random();
        double randomNumber = random.nextDouble() * (TOAST.length - 1);
        int randomToast = (int) Math.round(randomNumber);
        Toast.makeText(this, TOAST[randomToast], Toast.LENGTH_SHORT).show();
    }

    private void checkNfcStatus() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {
            Toast.makeText(this, "No NFC module", Toast.LENGTH_SHORT).show();
        } else if (nfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC module is enabled", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "NFC module is disabled", Toast.LENGTH_SHORT).show();
        }
    }

    private void enableOrDisableNfc() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {
            Toast.makeText(this, "No NFC module", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please enable or disable the NFC module!", Toast.LENGTH_LONG).show();
//        intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS); // for api < 16
            intent = new Intent(Settings.ACTION_NFC_SETTINGS);      // for api level 16 and above
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void openMailPage(String url) {
        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void startYouTube() {
        intent = getPackageManager().getLaunchIntentForPackage("com.google.android.youtube");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
