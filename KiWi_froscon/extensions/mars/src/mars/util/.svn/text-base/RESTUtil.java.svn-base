

package mars.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;


public final class RESTUtil {


    private RESTUtil() {
        // UNIMPEMENTED
    }

    public static String get(String user, String passwd, String uri) throws IOException {
        Authenticator.setDefault(new MyAuthenticator(user, passwd));


        final InputStream inputStream = getAsStream(user, passwd, uri);
        final BufferedReader reader =
            new BufferedReader(new InputStreamReader(inputStream));

        final StringBuffer result = new StringBuffer();
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            result.append(line);
        }
        reader.close();

        return result.toString();
    }

    public static InputStream getAsStream(String user, String passwd, String uri)
    throws IOException {
        Authenticator.setDefault(new MyAuthenticator(user, passwd));

        URL url = new URL(uri);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();


        final InputStream result = con.getInputStream();
        return result;
    }


    private static class MyAuthenticator extends Authenticator {


        final String username;

        final String password;

        private MyAuthenticator(String username, String password) {
            super();
            this.username = username;
            this.password = password;
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            getRequestingPrompt();
            getRequestingHost();
            getRequestingSite();
            getRequestingPort();

            final PasswordAuthentication result =
                new PasswordAuthentication(username, password.toCharArray());
            return result;
        }
    }

}

