/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ivan
 */
public enum DistributionsType {
    UNIFORM("Равномерное"),
    NORMAL("Нормальное"),
    POISSON("Пуассона"),
    ERLANG("Эрланга"),
    EXPONENTIAL("Экспоненциальное");
    private String displayName;
        
    DistributionsType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    // Получение enum по отображаемому имени
    public static DistributionsType fromDisplayName(String displayName) {
        for (DistributionsType type : values()) {
            if (type.displayName.equals(displayName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Неизвестное название: " + displayName);
    }
}

