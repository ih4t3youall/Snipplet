package pruebas;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import domain.SourceObject;

public class Inicio {

	public Inicio() {
		Set<SourceObject> set = new HashSet<SourceObject>();

		SourceObject nose[] = new SourceObject[3];
		SourceObject nose1[] = new SourceObject[3];

		nose[0] = new SourceObject("nombre", "mail", "asunto", "texto", "leido");
		nose[1] = new SourceObject("nombre1", "mail1", "asunto1", "texto1", "leido1");
		nose[2] = new SourceObject("nombre", "mail", "asunto", "texto", "leido1");

		nose1[0] = new SourceObject("nombre", "mail", "asunto", "texto", "leido");
		nose1[1] = new SourceObject("nombre2", "mail2", "asunto2", "texto2", "leido2");
		nose1[2] = new SourceObject("nombre", "mail", "asunto", "texto", "leido");

		 Object[] eliminarRepetidos = eliminarRepetidos(nose, nose1);
		 
		 
		 for (Object object : eliminarRepetidos) {
			
			 System.out.println(object.toString());
			 
		}

	}

	public Object[] eliminarRepetidos(Object[] vec, Object[] vec1) {

		int total = vec.length + vec1.length;
		Object[] arreglo = new Object[total];
		System.arraycopy(vec1, 0, arreglo, 0, vec1.length);
		System.arraycopy(vec, 0, arreglo, vec1.length, vec.length);

		Object[] aux = new Object[total];
		int contadorAux = 0;

		for (int i = 0; i < arreglo.length; i++) {

			boolean banderaEncontrado = false;

			for (int j = i + 1; j < arreglo.length; j++) {

				if (arreglo[i].toString().equals(arreglo[j].toString())) {
					banderaEncontrado = true;
					break;
				}

			}

			if (!banderaEncontrado) {
				aux[contadorAux] = arreglo[i];
				contadorAux++;

			}

		}

		return aux;

	}
}
