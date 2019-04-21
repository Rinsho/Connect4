package core;

/**
 * Represents a full column or invalid column number.
 * 
 * @author Jon McKee
 * @version 1.0
 */
public class InvalidColumnException extends Exception
{
    /**
     * Exception details message.
     */
    private final int columnNumber;

    /**
     * Initializes the exception message.
     * 
     * @param msg Exception details.
     * @param column Column that caused the exception.
     */
    public InvalidColumnException(String msg, int column)
    {
        super(msg);
        columnNumber = column;
    }

    /** Returns the column the exception occurred at.
     * 
     * @return The column number.
     */
    public int getColumn()
    {
        return columnNumber;
    }
}
