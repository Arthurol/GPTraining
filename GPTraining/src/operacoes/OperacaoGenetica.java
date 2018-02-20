package operacoes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import gptraining.inicializacao.GeradorArvoreMetodoGrow;
import gptraining.model.ArvoreExpressao;
import gptraining.model.CalculadorFitness;
import gptraining.model.Dataset;
import gptraining.model.No;
import gptraining.model.Operacao;
import gptraining.model.Populacao;

/*
 * Classe respons�vel pela modifica��o das �rvores de express�o atrav�s dos operadores gen�ticos.
 */
public class OperacaoGenetica {
	
	private int contadorOperacoesCrossover;
	private int contadorNosMutacao;
	private static double probabilidadeCrossover = 0.85;
	private static double probabilidadeTorneio = 0.7;
	private static int tamanhoTorneio = 10;
	
	public OperacaoGenetica()
	{
		contadorOperacoesCrossover = 0;
		contadorNosMutacao = 0;
	}
	
	/**
	 * Crossover onde peda�os de duas �rvores de express�o s�o usados para criar um descendente. Um ponto de crossover � escolhido nas duas �rvores
	 * 
	 */
	public ArvoreExpressao subtreeCrossover(ArvoreExpressao arvoreA, ArvoreExpressao arvoreB, Random random)
	{
		//Seleciona um ponto aleat�rio de crossover nas duas �rvores
		int pontoCrossoverA = random.nextInt(arvoreA.getQuantidadeOperadores(arvoreA.getRaiz()));
		int pontoCrossoverB = random.nextInt(arvoreB.getQuantidadeOperadores(arvoreB.getRaiz()));
		No subarvoreA = quebraNoPontoCrossover(arvoreA.getRaiz(), pontoCrossoverA);
		contadorOperacoesCrossover = 0;
		No subarvoreB = quebraNoPontoCrossover(arvoreB.getRaiz(), pontoCrossoverB);

		ArvoreExpressao ArvoreA = new ArvoreExpressao(subarvoreA);
		ArvoreExpressao ArvoreB = new ArvoreExpressao(subarvoreB);
		
		//Sorteio aleat�rio para decidir qual das ra�zes das duas subarvores ser� a raiz da �rvore originada
		int sorteio = random.nextInt(2);
		ArvoreExpressao arvoreCombinada = new ArvoreExpressao();
		
		//System.out.println("\n�rvores selecionadas para o crossover: \nArvore A: " + arvoreA.stringExpressao(arvoreA.getRaiz()));
		//System.out.println("Arvore B: " + arvoreB.stringExpressao(arvoreB.getRaiz()));
		
		if (sorteio == 0)
			arvoreCombinada = combina(ArvoreA, ArvoreB);
		else
			arvoreCombinada = combina(ArvoreB, ArvoreA);
		
		arvoreCombinada = arvoreCombinada.simplificarArvore(arvoreCombinada);
		
		//Se a �rvore gerada no crossover n�o possuir um terminal x, o crossover � feito novamente entre as duas �rvores iniciais.
		if (!arvoreCombinada.checaExistenciaDeNoX(arvoreCombinada.getRaiz()))
		{
			System.out.println("Inexist�ncia de terminal x na �rvore descendente, gerada do crossover entre " + arvoreA.stringExpressao(arvoreA.getRaiz()) + " e " + arvoreB.stringExpressao(arvoreB.getRaiz()));
			contadorOperacoesCrossover = 0;
			return null;
		}
		
		//System.out.println("�rvore descendente (A x B): " + arvoreCombinada.stringExpressao(arvoreCombinada.getRaiz()));
		contadorOperacoesCrossover = 0;
		arvoreCombinada.setExpressao(arvoreCombinada.stringExpressao(arvoreCombinada.getRaiz()));
		return arvoreCombinada;
	}
	
	/**
	 * Retorna a raiz da subarvore localizada no ponto de Crossover sorteado 
	 */
	public No quebraNoPontoCrossover(No no, int pontoCrossover)
	{
		if (no.getOperador() == null)
			return null;
		
	    if ((this.contadorOperacoesCrossover == pontoCrossover) && no.getOperador() != null)
	    	return new No(no);
	    
	    if ((this.contadorOperacoesCrossover != pontoCrossover) && no.getOperador() != null )
	    	this.contadorOperacoesCrossover++;
	    
	    No noQuebra = quebraNoPontoCrossover(no.getNoFilhoEsquerda(), pontoCrossover);
	    if (noQuebra == null)
	    	noQuebra = quebraNoPontoCrossover(no.getNoFilhoDireita(), pontoCrossover);
	    
	    return noQuebra;
    }
	
	/**
	 * M�todo que realiza uma muta��o de ponto(point mutation)
	 */
	public ArvoreExpressao mutacao(ArvoreExpressao arvore, Random random)
	{
		ArvoreExpressao arvoreMutada = new ArvoreExpressao(arvore.getRaiz());
		int quantidadeNos = arvore.getTamanhoArvore(arvore.getRaiz());
		int pontoMutacao = random.nextInt(quantidadeNos);
		
		//Muta��o com grande potencial de altera��o da �rvore
		No raizArvoreMutada = mutacaoPontoAleatorioRadical(arvoreMutada.getRaiz(), pontoMutacao, arvore.getProfundidade(arvore.getRaiz()), random);
		arvoreMutada = new ArvoreExpressao(raizArvoreMutada);
		
		//Muta��o de baixo impacto
		//mutacaoPontoAleatorioSimples(arvoreMutada.getRaiz(), pontoMutacao, random);
		
		arvoreMutada.simplificarArvore(arvoreMutada);
		arvoreMutada.setExpressao(arvoreMutada.stringExpressao(arvoreMutada.getRaiz()));
		contadorNosMutacao = 0;
		return arvoreMutada;
	}
	
	/**
	 * M�todo que percorre a �rvore at� encontrar o n� representado pelo ponto de muta��o. Ao ach�-lo, realiza sua substitui��o 
	 * por uma �rvore grow de tamanho m�ximo igual a altura da �rvore original.
	 */
	public No mutacaoPontoAleatorioRadical(No noRaiz, int pontoMutacao, int profundidadeArvoreOrigem, Random random) 
	{
		if (noRaiz == null)
			return null;
		
	    if (this.contadorNosMutacao == pontoMutacao)
	    {	
	    	GeradorArvoreMetodoGrow geradorArvoreGrow = new GeradorArvoreMetodoGrow();
			ArvoreExpressao arvoreGrow = geradorArvoreGrow.gerarArvore(profundidadeArvoreOrigem, random);
	    	
			if (pontoMutacao == 0 && noRaiz.possuiFilhos())
			{
				double sorteio = random.nextDouble();
				if (sorteio < 0.5)
					noRaiz.setNoFilhoEsquerda(new No(arvoreGrow.getRaiz()));
				else
					noRaiz.setNoFilhoDireita(new No(arvoreGrow.getRaiz()));
				
				return noRaiz;
			}
			else
			{
				this.contadorNosMutacao++;
				noRaiz = new No(arvoreGrow.getRaiz());
				return noRaiz;
			}
	    }
	   
	    if (this.contadorNosMutacao != pontoMutacao)
	    {
	    	this.contadorNosMutacao++;
	    	
	    	if (!noRaiz.possuiFilhos())
	    		return noRaiz;
	    	
	    	else
	    	{
	    		noRaiz.setNoFilhoEsquerda(mutacaoPontoAleatorioRadical(noRaiz.getNoFilhoEsquerda(), pontoMutacao, profundidadeArvoreOrigem, random));
			    noRaiz.setNoFilhoDireita(mutacaoPontoAleatorioRadical(noRaiz.getNoFilhoDireita(), pontoMutacao, profundidadeArvoreOrigem, random));
			    return noRaiz;
	    	}	
	    }    	
	    return null;
	}
	
	/**
	 * M�todo que percorre a �rvore at� encontrar o n� representado pelo ponto de muta��o. Ao ach�-lo, realiza sua substitui��o 
	 * por um elemento aleat�rio equivalente (operador por operador e terminal por terminal)
	 */
	public boolean mutacaoPontoAleatorioSimples(No noRaiz, int pontoMutacao, Random random) 
	{
		if (noRaiz == null)
			return false;
		
	    if (this.contadorNosMutacao == pontoMutacao)
	    {	
	    	if (noRaiz.getOperador() != null)
	    	{
	    		No novoOperador = new No();
	    		novoOperador.preenchimentoAleatorioOperador(random);
	    		noRaiz.setOperador(novoOperador.getOperador());
	    		this.contadorNosMutacao = 0;
	    		return true;
	    	}
	    	else if (noRaiz.getSimboloTerminal() != null)
	    	{
	    		No novoTerminal = new No();
	    		novoTerminal.preenchimentoAleatorioTerminal(random);
	    		noRaiz.setSimboloTerminal(novoTerminal.getSimboloTerminal());
	    		this.contadorNosMutacao = 0;
	    		return true;
	    	}
	    	else
	    		return false;    		
	    }
	   
	    if (this.contadorNosMutacao != pontoMutacao) 
	    	this.contadorNosMutacao++;
	    
	    if (!mutacaoPontoAleatorioSimples(noRaiz.getNoFilhoEsquerda(), pontoMutacao, random))
	    {
	    	return mutacaoPontoAleatorioSimples(noRaiz.getNoFilhoDireita(), pontoMutacao, random);
	    }

		return true;
	}

	/**
	 * Expectativa: M�todo que chamar� o c�lculo de fitness sobre uma gera��o (List<ArvoreExpressao>) e a ordenar� pela fitness.
	 * Ap�s ocorrer� o sorteio para determinar se algum operador gen�tico (crossover ou muta��o) ser� aplicado sobre os indiv�duos de melhor fitness.
	 * � importante lembrar que o crossover deve ter uma probabilidade de ocorrer bem maior do que a muta��o.
	 */
	public Populacao selecao(Populacao populacao, Dataset dataset, Random random)
	{
		CalculadorFitness calculador = new CalculadorFitness();
		
		//faz o calculo do fitness de toda a popula��o		
		for (int i = 0; i < populacao.getIndividuos().size(); i++)
		{
			double aptidao = calculador.calcula(populacao.getIndividuos().get(i), dataset);
			populacao.getIndividuos().get(i).setAptidao(aptidao == -1 ? 1000000 : aptidao);
		}
		
		//Ordena a popula��o pela aptid�o (fitness). Quanto mais pr�xima de zero for a aptid�o, melhor � a �rvore (Dist�ncia entre dois pontos)
		Collections.sort(populacao.getIndividuos());
		
		Populacao proximaGeracao = new Populacao();
		proximaGeracao.setNumeroGeracao(populacao.getNumeroGeracao() + 1);
		int tamanhoPopulacao = populacao.getIndividuos().size();
		double tamanhoElite = (tamanhoPopulacao * 0.3);
		for (int i = 0; i < tamanhoElite; i++)
		{
			System.out.println("Arvore elite " + (i+1) + ": "  + populacao.getIndividuos().get(i).stringExpressao(populacao.getIndividuos().get(i).getRaiz()) + "----------->" + populacao.getIndividuos().get(i).getAptidao());
			proximaGeracao.adicionaIndividuo(populacao.getIndividuos().get(i));	
		}

		while(proximaGeracao.getIndividuos().size() < populacao.getIndividuos().size())
		{
			/*
			System.out.println("\n\nGera��o: Estado atual:");
			for (int i = 0; i < proximaGeracao.getIndividuos().size(); i++)
			{
				System.out.println("Arvore " + (i+1) + ": "  + proximaGeracao.getIndividuos().get(i).stringExpressao(proximaGeracao.getIndividuos().get(i).getRaiz()) + "----------->" + proximaGeracao.getIndividuos().get(i).getAptidao());
			}
			System.out.println("\n\n");
			*/
			
			double escolhaOperadorGenetico = random.nextDouble();
			ArvoreExpressao arvoreProduto = new ArvoreExpressao();
			List<ArvoreExpressao> vencedoresTorneio = new ArrayList<ArvoreExpressao>();
			
			if (escolhaOperadorGenetico < probabilidadeCrossover)
			{
				while(true)
				{
					/* Crossover Torneio Customizado 
					List<ArvoreExpressao> candidatosA = torneioCustomizado(populacao, random);
					List<ArvoreExpressao> candidatosB = torneioCustomizado(populacao, random);
					int paiA = random.nextInt(candidatosA.size());
					int paiB = random.nextInt(candidatosB.size());
					ArvoreExpressao arvoreA = new ArvoreExpressao(candidatosA.get(paiA).getRaiz());
					ArvoreExpressao arvoreB = new ArvoreExpressao(candidatosB.get(paiB).getRaiz());
					
					try
					{
						if (arvoreA == null || arvoreB == null)
						{
							throw new Exception("Um dos selecionados para o crossover � null");
						}
					
						arvoreA.setAptidao(calculador.calcula(arvoreA, dataset) == -1 ? 1000000 : calculador.calcula(arvoreA, dataset));
						arvoreB.setAptidao(calculador.calcula(arvoreB, dataset) == -1 ? 1000000 : calculador.calcula(arvoreB, dataset));
						
						
						
						//arvoreProduto = new ArvoreExpressao(subtreeCrossover(arvoreA, arvoreB, random));
						arvoreProduto = new ArvoreExpressao(subtreeCrossover(arvoreA, arvoreB, random).getRaiz());

						if (arvoreProduto != null)
						{
							if (arvoreProduto.checaValidadeArvore(arvoreProduto.getRaiz()) && arvoreProduto.checaExistenciaDeNoX(arvoreProduto.getRaiz()))
							{
								break;
							}
						}
					} catch (Exception e)
					{
						System.out.println("Exce��o no crossover entre as �rvores:");
						System.out.println(arvoreA.stringExpressao(arvoreA.getRaiz()) + " e " + arvoreB.stringExpressao(arvoreB.getRaiz()));
					}
					*/
					
					//Crossover Torneio Cl�ssico
					try
					{
						vencedoresTorneio = torneioClassico(tamanhoTorneio, probabilidadeTorneio, populacao, random, 2);
						arvoreProduto = new ArvoreExpressao(subtreeCrossover(vencedoresTorneio.get(0), vencedoresTorneio.get(1), random).getRaiz());
						
						if (arvoreProduto != null)
						{
							if (arvoreProduto.checaValidadeArvore(arvoreProduto.getRaiz()) && arvoreProduto.checaExistenciaDeNoX(arvoreProduto.getRaiz()))
							{
								break;
							}
						}
					} catch (Exception e)
					{
						if (vencedoresTorneio.isEmpty())
						{
							System.out.println("Exce��o no crossover entre as �rvores:");
							System.out.println(vencedoresTorneio.get(0).stringExpressao(vencedoresTorneio.get(0).getRaiz()) + " e " + vencedoresTorneio.get(1).stringExpressao(vencedoresTorneio.get(1).getRaiz()));
						}
					}
				}
			}
			else //sorteio determinou que o operador gen�tico ser� muta��o
			{
				while(true)
				{
					//Muta��o Torneio Customizado
					//List<ArvoreExpressao> candidatosMutacao = torneioCustomizado(populacao, random);
					//arvoreProduto = new ArvoreExpressao(mutacao(candidatosMutacao.get(random.nextInt(candidatosMutacao.size())), random).getRaiz());
					
					//Muta��o Torneio Cl�ssico
					vencedoresTorneio = torneioClassico(tamanhoTorneio, probabilidadeTorneio, populacao, random, 1);
					arvoreProduto = new ArvoreExpressao(mutacao(vencedoresTorneio.get(0), random).getRaiz());
					
					if (arvoreProduto.checaValidadeArvore(arvoreProduto.getRaiz()) && arvoreProduto.checaExistenciaDeNoX(arvoreProduto.getRaiz()))
						break;
				}
			}		
			
			double aptidao = calculador.calcula(arvoreProduto, dataset);
			arvoreProduto.setAptidao(aptidao == -1 ? 1000000 : aptidao);
			proximaGeracao.adicionaIndividuo(arvoreProduto);	
		}
	
		/* 
		for (ArvoreExpressao arv : proximaGeracao.getIndividuos())
		{
			double aptidao = calculador.calcula(arv, dataset);
			arv.setAptidao(aptidao == -1 ? 1000000 : aptidao);
		}
		*/
		
		Collections.sort(proximaGeracao.getIndividuos());

		System.out.println("\n\n <GERA��O " + String.valueOf(proximaGeracao.getNumeroGeracao())  + "-------------------------------------------------->");
		for (ArvoreExpressao arvoreExpressao : proximaGeracao.getIndividuos())
		{
			System.out.println(arvoreExpressao.stringExpressao(arvoreExpressao.getRaiz()) + " --> " + arvoreExpressao.getAptidao());
		}
		
		return proximaGeracao;
	}
	
	/**
	 * Substitui aleatoriamente um dos n�s filhos da subarvore A pela subarvore B e retorna a �rvore resultante.
	 */
	public ArvoreExpressao combina(ArvoreExpressao subarvoreA, ArvoreExpressao subarvoreB)
	{
		ArvoreExpressao arvoreResultado = new ArvoreExpressao(subarvoreA.getRaiz());
		arvoreResultado.getRaiz().setNoFilhoEsquerda(subarvoreB.getRaiz());
			
		return arvoreResultado;
	}
	
	private List<ArvoreExpressao> torneioCustomizado(Populacao populacao, Random random)
	{
		double escolhaGrupo = random.nextDouble();
		List<ArvoreExpressao> grupoTorneio = new ArrayList<ArvoreExpressao>();
		
		
		//Torneio formado pela metade da popula��o. Subdivis�o dessa metade em cinco grupos de quantidade igual de indiv�duos. 
		//A probabilidade de escolha de cada grupo � a seguinte: g1 - 40%, g2 - 20%, g3 - 20%, g4 - 10, g5 - 10%
		if (escolhaGrupo >= 0 && escolhaGrupo < 0.4)
			grupoTorneio = getGrupoParticipantes(1, populacao.getIndividuos());
		
		else if (escolhaGrupo >= 0.4 && escolhaGrupo < 0.6)
			grupoTorneio = getGrupoParticipantes(2, populacao.getIndividuos());
			
		else if (escolhaGrupo >= 0.6 && escolhaGrupo < 0.8)
			grupoTorneio = getGrupoParticipantes(3, populacao.getIndividuos());
			
		else if (escolhaGrupo >= 0.8 && escolhaGrupo < 0.9)
			grupoTorneio = getGrupoParticipantes(4, populacao.getIndividuos());
			
		else if (escolhaGrupo >= 0.9 && escolhaGrupo < 1.0)
			grupoTorneio = getGrupoParticipantes(5, populacao.getIndividuos());
		
		return grupoTorneio;
	}
	
	/**
	 * Divis�o abstrata da primeira metade da popula��o passada nos par�metros em cinco grupos de quantidade igual de indiv�duos. 
	 * O m�todo retorna o grupo de indiv�duos correspondente ao inteiro passado (chamada com numeroGrupo == 1 retorna os primeiros 10% de indiv�duos)
	 */
	private List<ArvoreExpressao> getGrupoParticipantes(int numeroGrupo, List<ArvoreExpressao> individuosPopulacao)
	{
		int tamanhoGrupo = individuosPopulacao.size()/10; //quinta parte da metade da popula��o
		List<ArvoreExpressao> grupoSelecionado = new ArrayList<ArvoreExpressao>();
		
		for(int i = numeroGrupo * tamanhoGrupo; i < (numeroGrupo * tamanhoGrupo) + tamanhoGrupo; i++)
		{
			grupoSelecionado.add(individuosPopulacao.get(i - 1));
		}

		return grupoSelecionado;
	}
	
	/**
	 * Implementa��o de torneio cl�ssico. Um determinado numero de indiv�duos � selecionado da popula��o para participar do torneio. 
	 * A probabilidade p equivale a chance do indiv�duo com melhor aptid�o ser selecionado deste grupo. O segundo melhor individuo tem chance equivalente a p*(1-p), o terceiro a p*(1-p)� e assim por diante.
	 */
	private List<ArvoreExpressao> torneioClassico (int tamanhoTorneio, double probabilidade, Populacao populacao, Random random, int numeroVencedores)
	{
		if (probabilidade > 1)
		{
			System.out.println("Valor da probabilidade p maior que 1");
			return null;
		}
			
		List<ArvoreExpressao> participantes = new ArrayList<ArvoreExpressao>();
		List<ArvoreExpressao> vencedores = new ArrayList<ArvoreExpressao>();
		List<Integer> sorteioIndices = new ArrayList<Integer>();
		
		for (int i = 0; i < populacao.getIndividuos().size(); i++)
		{
			sorteioIndices.add(i);
		}
		Collections.shuffle(sorteioIndices);
		
		for (int i = 0; i < tamanhoTorneio; i++)
		{
			participantes.add(populacao.getIndividuos().get(sorteioIndices.get(i)));
		}
		
		Collections.sort(participantes);
		
		double p = probabilidade;
		int indiceVencedor = 0;
		for (int i = 0; i < numeroVencedores; i++)
		{
			double sorteio = random.nextDouble();
			double pLocal = p;
			int expoente = 1;
			
			while(true)
			{
				if (sorteio < pLocal)
				{
					vencedores.add(participantes.get(indiceVencedor));
					participantes.remove(indiceVencedor);
					break;
				}
				else
				{
					pLocal += (p * Math.pow(1 - p, expoente));
					indiceVencedor++;
					expoente++;
				}
			}
			
			indiceVencedor = 0;
		}
		return vencedores;
	}
}