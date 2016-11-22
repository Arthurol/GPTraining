package gptraining.model;

import java.util.Random;

import lombok.Getter;
import lombok.Setter;

/**
 * Classe que abriga algumas operações referente às árvores sintéticas e aos nós.
 */
public class ArvoreSintaticaExpressaoMatematica implements Comparable<ArvoreSintaticaExpressaoMatematica>
{
	private @Getter @Setter No raiz;
	private @Getter int profundidade;
	private @Getter @Setter int aptidao;
	
	public ArvoreSintaticaExpressaoMatematica(No raiz)
	{
		this.raiz = raiz;
		this.profundidade = raiz.getProfundidade();
		this.aptidao = 0;
	}
	
	public ArvoreSintaticaExpressaoMatematica(int profundidade)
	{
		try
		{
			this.profundidade = profundidade;
			this.aptidao = 0;
			
			if (profundidade == 1)
			{
				raiz = new No(1);
				raiz.noFilhoEsquerda = new No(0);
				raiz.noFilhoDireita = new No(0);
				
			} else if (profundidade > 1)
			{
				raiz = new No(profundidade);
			
			} else
				throw new Exception ("Uma árvore foi inicializa com profundidade inferior a 1.");
			
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	public ArvoreSintaticaExpressaoMatematica()
	{
		this.raiz = null;
		this.profundidade = -1;
		this.aptidao = 0;
	}

	/**
	 * Geração de uma árvore/expressão aleatória através do método Grow. Neste a profundidade da árvore não precisa necessáriamente corresponder ao seu limite máximo, 
	 * além da possibilidade de haver folhas em níveis superiores à profundidade da árvore.
	 */
	public No geraArvoreMetodoGrow(int profundidadeLimite)
	{
		
		Random random = new Random();
		if (profundidadeLimite < 1)
			return null;
		
		if (profundidadeLimite == 1)
			return new ArvoreSintaticaExpressaoMatematica(1).getRaiz();
		
		No raiz = new No();
		raiz.setNoFilhoEsquerda(new No());
		raiz.setNoFilhoDireita(new No());
		int decisao = random.nextInt(4);
		
		switch (decisao)
		{
		case 0:
			raiz.setNoFilhoEsquerda(geraArvoreMetodoGrow(profundidadeLimite - 1));
			raiz.setNoFilhoDireita(new No(0));
			raiz.setProfundidade(raiz.getNoFilhoEsquerda().getProfundidade() + 1);
			break;
			
		case 1:
			raiz.setNoFilhoEsquerda(new No(0));
			raiz.setNoFilhoDireita(geraArvoreMetodoGrow(profundidadeLimite - 1));
			raiz.setProfundidade(raiz.getNoFilhoDireita().getProfundidade() + 1);
			break;
			
		case 2:
			raiz.setNoFilhoEsquerda(geraArvoreMetodoGrow(profundidadeLimite - 1));
			raiz.setNoFilhoDireita(geraArvoreMetodoGrow(profundidadeLimite - 1));
			raiz.setProfundidade(Math.max(raiz.getNoFilhoDireita().getProfundidade() + 1 , raiz.getNoFilhoDireita().getProfundidade() + 1));
			break;
			
		case 3:
			return geraArvoreMetodoGrow(1);
	
		}

		return raiz;
	}
	
	/**
	 * Geração de uma árvore aleatória completa, onde a profundidade é a maior possível, de acordo com o limite de profundidade estabelecido.
	 * Os terminais só podem ser observados no último nível da árvore. 
	 */
	public No geraArvoreMetodoFull(int profundidadeLimite)
	{
		
		Random random = new Random();
		if (profundidadeLimite < 1)
			return null;
		
		if (profundidadeLimite == 1)
			return new ArvoreSintaticaExpressaoMatematica(1).getRaiz();
		
		else
		{
			No raiz = new No(profundidadeLimite);
			raiz.setNoFilhoEsquerda(geraArvoreMetodoFull(profundidadeLimite - 1));
			raiz.setNoFilhoDireita(geraArvoreMetodoFull(profundidadeLimite - 1));
		}
		
		return raiz;
	}
	
	
	/**
	 * Recursivamente resolve as operações matemáticas da árvore, representada pelo nó raiz, retornando um resultado double convertido em String. 
	 * Como há recursão, o tipo de retorno escolhido foi String, pois além de inteiros entre -9 e 9 pode haver x no símbolo de um nó.
	 */
	public String resolverExpressao(No raizSubArvore, int valorX)
	{
		if (raizSubArvore.getNoFilhoEsquerda() != null && raizSubArvore.getNoFilhoDireita() != null)
		{
			No filhoEsquerda = raizSubArvore.noFilhoEsquerda;
			No filhoDireita = raizSubArvore.noFilhoDireita;
			Operador operador = raizSubArvore.getOperador();
			
			if (filhoEsquerda.tipoDeNo == TipoDeNo.folha && filhoDireita.tipoDeNo == TipoDeNo.folha)
			{
				return resolverOperacao(filhoEsquerda.getSimboloTerminal(), filhoDireita.getSimboloTerminal(), operador, valorX);		
			}
			
			if (filhoEsquerda.tipoDeNo == TipoDeNo.interno && filhoDireita.tipoDeNo == TipoDeNo.folha)
			{
				return resolverOperacao(resolverExpressao(filhoEsquerda, valorX), filhoDireita.getSimboloTerminal(), operador, valorX);
			}
			
			if (filhoEsquerda.tipoDeNo == TipoDeNo.folha && filhoDireita.tipoDeNo == TipoDeNo.interno)
			{
				return resolverOperacao(filhoEsquerda.getSimboloTerminal(), resolverExpressao(filhoDireita, valorX), operador, valorX);
			}
			
			if (filhoEsquerda.tipoDeNo == TipoDeNo.interno && filhoDireita.tipoDeNo == TipoDeNo.interno)
			{
				return resolverOperacao(resolverExpressao(filhoEsquerda, valorX), resolverExpressao(filhoDireita, valorX), operador, valorX);
			}
		}
		return null;
	}
	
	/**
	 * Retorna, na forma de String, o resultado de uma operação entre dois terminais.
	 */
	public String resolverOperacao(String terminalEsquerda, String terminalDireita, Operador operador, int valorX)
	{
		double numeroEsquerda = Double.valueOf(terminalEsquerda);
		double numeroDireita = Double.valueOf(terminalDireita);
		
		try 
		{
			if (checaCaracterValido(terminalEsquerda, terminalDireita) == false)
				throw new Exception ("O nó contém um terminal que não é um inteiro entre -9 e 9 e não é igual a x.");
				
			if (terminalEsquerda == "x" || terminalEsquerda == "X")
				numeroEsquerda = (double) valorX;
			
			if (terminalDireita == "x" || terminalDireita == "X")
				numeroDireita = (double) valorX;
			
			switch (operador)
			{
				case soma:
					return String.valueOf(numeroEsquerda + numeroDireita);
					
				case subtracao:
					return String.valueOf(numeroEsquerda - numeroDireita);
					
				case multiplicacao:
					return String.valueOf(numeroEsquerda * numeroDireita);
					
				case divisao:
					if (numeroDireita == 0.0)
						throw new Exception ("Uma divisão por 0 foi encontrada. Execução cancelada.");
					
					return String.valueOf (numeroEsquerda / numeroDireita);		
			}
			return null;
			
		} catch (Exception e)
		{
			System.out.println(e);
			return e.getMessage();
		}
	}
	
	public boolean checaCaracterValido(String terminalEsquerda, String terminalDireita)
	{
		if ((Double.valueOf(terminalEsquerda) < -9.0 || Double.valueOf(terminalEsquerda) > 9.0 || 
				Double.valueOf(terminalDireita) < -9.0 || Double.valueOf(terminalEsquerda) > 9.0 ) && 
					(terminalEsquerda != "x" || terminalEsquerda != "X" || terminalDireita != "x" || terminalDireita != "X" ))
		return false;
		
		else
			return true;
	}
	
	/**
	 * Garante a presença do terminal "x" na árvore, pois a mesma deve estar em função de "x".
	 */
	public boolean checaExistenciaDeNoX(No raiz)
	{
		if (raiz.getSimboloTerminal() != null && (raiz.getSimboloTerminal().equals("x") || raiz.getSimboloTerminal().equals("X")))
			return true;
		else 
		{
			if (raiz.getNoFilhoEsquerda() != null)
			{
				if (checaExistenciaDeNoX(raiz.getNoFilhoEsquerda()))
					return true;
			}
			if (raiz.getNoFilhoDireita() != null)
			{
				if (checaExistenciaDeNoX(raiz.getNoFilhoEsquerda()))
					return true;
			}
			
			return false;
		}
	}
	
	/**
	 * Se a expressão contida na árvore resulta em Y, dado seu valor de X correspondente, há um ganho na aptidão da árvore/expressão em questão.
	 */
	public int avaliarAptidaoArvore(No raiz, int[] vetX, int[] vetY)
	{
		int aptidao = 0;
		for (int i = 0; i < vetX.length; i++)
		{
			if (vetY[i] == Integer.parseInt(resolverExpressao(raiz, vetX[i])))
					aptidao ++;
		}
		
		return aptidao;
	}
	
	public int compareTo(ArvoreSintaticaExpressaoMatematica outraArvore)
	{
		if (this.aptidao < outraArvore.aptidao)
			return -1;
		
		if (this.aptidao > outraArvore.aptidao)
			return 1;
			
		return 0;
	}
}
