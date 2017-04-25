package im.hua.artofandroid.keystore;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.security.KeyPairGeneratorSpec;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.security.auth.x500.X500Principal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import im.hua.artofandroid.R;

public class KeyStoreActivity extends AppCompatActivity {

    @BindView(R.id.et_alias)
    EditText mEtAlias;
    @BindView(R.id.et_content)
    EditText mEtContent;
    @BindView(R.id.tv_result)
    TextView mTvResult;

    private KeyStore mKeystore;

    public static class KSHandler extends Handler{

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_store);
        ButterKnife.bind(this);

        try {
            mKeystore = KeyStore.getInstance("AndroidKeyStore");
            mKeystore.load(null);

            getAliases();


        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.btn_new)
    public void addNewAlias(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            createKey(mEtAlias.getText().toString());
        }
    }

    @OnClick(R.id.btn_encrypt)
    public void encrypt(View view) {
        encrypt(mEtAlias.getText().toString());
    }

    @OnClick(R.id.btn_decrypt)
    public void decrypt(View view) {
        decrypt(mEtAlias.getText().toString());
    }

    private void getAliases() throws KeyStoreException {
        List<String> aliaseList = new ArrayList<>();
        Enumeration<String> aliases = mKeystore.aliases();
        StringBuilder sb = new StringBuilder();
        while (aliases.hasMoreElements()) {
            String element = aliases.nextElement();
            aliaseList.add(element);
            sb.append(element);
            sb.append("\n");
        }
        Log.d("KeyStoreActivity", sb.toString());
        mTvResult.setText(sb.toString());
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void createKey(String alias) {
        try {
            if (!mKeystore.containsAlias(alias)) {
                Calendar start = Calendar.getInstance();
                Calendar end = Calendar.getInstance();
                end.add(Calendar.YEAR, 30);
                KeyPairGeneratorSpec generatorSpec = new KeyPairGeneratorSpec.Builder(this)
                        .setAlias(alias)
                        .setSubject(new X500Principal("CN=Sample Name, O=Android Authority"))
                        .setSerialNumber(BigInteger.ONE)
                        .setStartDate(start.getTime())
                        .setEndDate(end.getTime())
                        .build();

                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
                keyPairGenerator.initialize(generatorSpec);
                KeyPair keyPair = keyPairGenerator.generateKeyPair();
                Log.d("KeyStoreActivity", keyPair.getPublic().toString());
                mTvResult.setText(keyPair.getPublic().toString());
            }
        } catch (KeyStoreException | NoSuchAlgorithmException | NoSuchProviderException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    private void encrypt(String alias) {
        try {
            KeyStore.PrivateKeyEntry entry = (KeyStore.PrivateKeyEntry) mKeystore.getEntry(alias, null);
            RSAPublicKey publicKey = (RSAPublicKey) entry.getCertificate().getPublicKey();

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, cipher);
            cipherOutputStream.write(mEtContent.getText().toString().getBytes("UTF-8"));
            cipherOutputStream.close();

            byte[] bytes = outputStream.toByteArray();
            mTvResult.setText(Base64.encodeToString(bytes, Base64.DEFAULT));

            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void decrypt(String alias) {
        KeyStore.PrivateKeyEntry entry = null;
        try {
            entry = (KeyStore.PrivateKeyEntry) mKeystore.getEntry(alias, null);
            RSAPrivateKey privateKey = (RSAPrivateKey) entry.getPrivateKey();
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            CipherInputStream cipherInputStream = new CipherInputStream(new ByteArrayInputStream(Base64.decode(mTvResult.getText().toString(), Base64.DEFAULT)), cipher);
            ArrayList<Byte> values = new ArrayList<>();
            int nextByte;
            while ((nextByte = cipherInputStream.read()) != -1) {
                values.add((byte) nextByte);
            }

            byte[] bytes = new byte[values.size()];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = values.get(i).byteValue();
            }

            String finalText = new String(bytes, 0, bytes.length, "UTF-8");
            mTvResult.setText(finalText);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
