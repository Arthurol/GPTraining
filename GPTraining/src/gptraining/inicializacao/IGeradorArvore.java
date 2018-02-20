package gptraining.inicializacao;

import java.util.Random;

import gptraining.model.ArvoreExpressao;

public interface IGeradorArvore {

	public ArvoreExpressao gerarArvore(int profundidadeLimite, Random random);
	
}
