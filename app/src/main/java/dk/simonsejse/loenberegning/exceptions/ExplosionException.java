package dk.simonsejse.loenberegning.exceptions;

public class ExplosionException extends RuntimeException{
    public ExplosionException(String errorMessage) {
        super(errorMessage);
    }
}
