public class Recruiter extends Employee {
    private Double rating;

    public Recruiter(Resume resume, String company, int salary, Double rating) {
        super(resume, company, salary);
        this.rating = rating;
    }

    public int evaluate(Job job, User user) {
        int score = (int) Math.ceil(rating * user.getTotalScore());
        rating += 0.1;
        Company company = Application.getInstance().getCompany(getCompanyName());
        company.getManager().addRequest(new Request<>(job, user, this, (double) score));
        return score;
    }

    public Double getRating() {
        return rating;
    }

    @Override
    public String toString() {
        String string = "";
        string += getResume().getInfo().toString();
        string += "Salary: " + getSalary() + "\n";
        string += "Rating: " + rating + "\n";
        string += getResume().toString(false);
        return string;
    }

    @Override
    public String toJSON() {
        String string = "";
        string += getResume().getInfo().toJSON();
        string += "\t\t\t\"salary\": " + getSalary() + ",\n";
        string += "\t\t\t\"rating\": " + rating + ",\n";
        string += getResume().toJSON(false);
        return string;
    }
}
