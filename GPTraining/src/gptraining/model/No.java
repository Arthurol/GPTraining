package gptraining.model;

import java.util.Random;

import lombok.Getter;
import lombok.Setter;

/**
 * Estrutura das árvores sintéticas, que carrega um elemento do conjunto de terminais ou do conjunto de funções.
 */
public class No 
{
	private @Getter @Setter String simboloTerminal;
	private @Setter Operador operador;
	public @Getter @Setter No noFilhoEsquerda;
	public @Getter @Setter No noFilhoDireita;
	
	public No()
	{
		this.simboloTerminal = null;
		this.operador = null;
		this.noFilhoEsquerda = null;
		this.noFilhoDireita = null;
	}
	
	
	public Operador getOperador()
	{
		return (operador.getOperador(operador.getCodigo()));
	}
	
	public boolean possuiFilhos()
	{
		if (this.noFilhoEsquerda != null || this.noFilhoDireita != null)
			return true;
		
		return false;
	}
	
	public void preenchimentoAleatorioOperador()
	{
		Random random = new Random();
		this.noFilhoEsquerda = new No();
		this.noFilhoDireita = new No();
		
		Operador operadorEscolhido = Operador.soma;
		int numDecisaoOperador = random.nextInt(4);
		this.operador = operadorEscolhido.getOperador(numDecisaoOperador);
	}
	
	public void preenchimentoAleatorioTerminal()
	{
		Random random = new Random();
		String terminalEscolhido = null;
		int numDecisaoTerminal = random.nextInt(12);
		int terminalInt = numDecisaoTerminal % 12;

		/* A chance do terminal ser a variável X é o quádruplo da chance dos outros terminais. Os sorteios 10 e 11 resultarão na 
		* atribuição de "X" ao terminal. Sorteios menores (de 0 a 9) passarão por um novo processo de aleatoriedade para decidir
		* o sinal do terminal. 
		*/
		if (terminalInt > 9)
		{
			terminalEscolhido = "x";
			
		} else
		{
			//sorteio para decidir se o número terminal é negativo ou positivo
			boolean decisao = (random.nextInt() % 2 == 0? true : false);
			
			if (decisao == false)
				terminalInt *= -1;
			
			terminalEscolhido = String.valueOf(terminalInt);
		}
		
		this.simboloTerminal = terminalEscolhido;
		this.noFilhoEsquerda = null;
		this.noFilhoDireita = null;
		this.operador = null;
	}
}
