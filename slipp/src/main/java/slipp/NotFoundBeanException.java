package slipp;

public class NotFoundBeanException extends RuntimeException {
    private static final String MESSAGE = "해당 Bean이 존재하지 않습니다. : ";

    public NotFoundBeanException(String beanClass) {
        super(MESSAGE + beanClass);
    }
}
