package persistencia;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class CustomObjectOutputStream extends ObjectOutputStream
{
    /** Constructor que recibe OutputStream */
    public CustomObjectOutputStream(OutputStream out) throws IOException
    {
        super(out);
    }

    /** Constructor sin par�metros */
    protected CustomObjectOutputStream() throws IOException, SecurityException
    {
        super();
    }

    /** Redefinici�n del m�todo de escribir la cabecera para que no haga nada. */
    protected void writeStreamHeader() throws IOException
    {
    }

}
