package gptraining.model;

import java.util.Random;

import lombok.Getter;
import lombok.Setter;

/**
 * Estrutura das �rvores sint�ticas, que carrega um elemento do conjunto de terminais ou do conjunto de fun��es.
 */
public class No 
{
	private @Getter @Setter String simboloTerminal;
	private @Setter Operacao operador;
	public @Getter @Setter No noFilhoEsquerda;
	public @Getter @Setter No noFilhoDireita;
	
	public No()
	{
		this.simboloTerminal = null;
		this.operador = null;
		this.noFilhoEsquerda = null;
		this.noFilhoDireita = null;
	}
	
	public No(char operador, No noEsquerda, No noDireita)
	{
		this.simboloTerminal = "";
		this.operador = Operacao.get(operador);
		this.noFilhoEsquerda = noEsquerda;
		this.noFilhoDireita = noDireita;
	}
	
	public No(String terminal)
	{
		this.simboloTerminal = terminal;
		this.operador = null;
		this.noFilhoEsquerda = null;
		this.noFilhoDireita = null;
	}
	
	public No(double terminal)
	{
		this.simboloTerminal = String.format("%d", terminal);
		this.operador = null;
		this.noFilhoEsquerda = null;
		this.noFilhoDireita = null;
	}

	public Operacao getOperador()
	{
		return operador;
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
		int numDecisaoOperador = random.nextInt(4);
		this.operador = Operacao.get(numDecisaoOperador);
	}
	
	public void preenchimentoAleatorioTerminal()
	{
		Random random = new Random();
		String terminalEscolhido = null;
		int numDecisaoTerminal = random.nextInt(12);
		int terminalInt = numDecisaoTerminal % 12;

		/* A chance do terminal ser a vari�vel X � o qu�druplo da chance dos outros terminais. Os sorteios 10 e 11 resultar�o na 
		* atribui��o de "X" ao terminal. Sorteios menores (de 0 a 9) passar�o por um novo processo de aleatoriedade para decidir
		* o sinal do terminal. 
		*/
		if (terminalInt > 9)
		{
			terminalEscolhido = "x";
			
		} else
		{
			//sorteio para decidir se o n�mero terminal � negativo ou positivo
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

	public No getNoFilhoEsquerda() {
		
		return noFilhoEsquerda;
	}
	
	public No getNoFilhoDireita() {
		
		return noFilhoDireita;
	}

	public String getSimboloTerminal() {
		
		return simboloTerminal;
	}

	public void setNoFilhoEsquerda(No noFilhoEsquerda) {
		this.noFilhoEsquerda = noFilhoEsquerda;
	}

	public void setNoFilhoDireita(No noFilhoDireita) {
		this.noFilhoDireita = noFilhoDireita;
	}
}