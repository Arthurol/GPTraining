package gptraining.model;

import java.util.Random;
import lombok.Getter;
import lombok.Setter;

/**
 * Operadores matemáticos, usados nos nós internos da árvore para representar expressões matemáticas.
 */
public enum Operador 
{
	soma(0),
	multiplicacao(1),
	subtracao(2),
	divisao(3);
	
	public @Getter int codigo;

	private Operador()
	{
		Random random = new Random();
		int num = random.nextInt(4);
		this.codigo = num;
	}
	
	private Operador(int codigo)
    {
        this.codigo = codigo;
    }
	
	public Operador getOperador(int codigo)
	{
		switch(codigo)
		{
		case 0:
			return Operador.soma;
		
		case 1:
			return Operador.multiplicacao;
			
		case 2:
			return Operador.subtracao;
			
		case 3:
			return Operador.divisao;			
		}
		
		return null;
	}
	
}

