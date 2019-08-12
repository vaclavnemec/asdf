package zvn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
class Loan {
    private String id;
    private String story;
    private BigDecimal amount;
    private Date datePublished;

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Date getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(Date datePublished) {
        this.datePublished = datePublished;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "id='" + id + '\'' +
                ", story='" + story + '\'' +
                ", amount=" + amount +
                ", datePublished=" + datePublished +
                '}';
    }
}
