/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ivan
 */
public class InvalidNormalDistributionVarianceParameterException extends IllegalArgumentException {
    public InvalidNormalDistributionVarianceParameterException(String message) {
        super(message);
    }
    
    public InvalidNormalDistributionVarianceParameterException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public InvalidNormalDistributionVarianceParameterException(Throwable cause) {
        super(cause);
    }
}
