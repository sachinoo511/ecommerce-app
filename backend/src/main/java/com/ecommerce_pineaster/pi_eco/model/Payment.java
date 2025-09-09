package com.ecommerce_pineaster.pi_eco.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Entity
@Table(name = "payments")
@NoArgsConstructor
@AllArgsConstructor
public class Payment{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @OneToOne(mappedBy = "payment", fetch = FetchType.LAZY)
    private  Order order;

    @NotBlank
    @Size(min = 1,message = "Pyment method contain at leas 5 character")
    private  String paymentType;
    private String pgPaymentId;
    private String pgStatus;
    private String pgResponseMessage;
    private String pgName;

    public Payment(String paymentType, String pgPaymentId, String pgStatus, String pgResponseMessage, String pgName) {
        this.paymentType = paymentType;
        this.pgPaymentId = pgPaymentId;
        this.pgStatus = pgStatus;
        this.pgResponseMessage = pgResponseMessage;
        this.pgName = pgName;

    }
}