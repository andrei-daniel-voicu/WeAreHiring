import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.TreeSet;

public abstract class Consumer {
    private final Resume resume;
    private final ArrayList<Consumer> relatives;

    public Consumer(Resume resume) {
        this.resume = resume;
        relatives = new ArrayList<>();
    }

    public Consumer(Resume resume, ArrayList<Consumer> relatives) {
        this.resume = resume;
        this.relatives = relatives;
    }

    public void add(Education education) {
        resume.education.add(education);
    }

    public void add(Experience experience) {
        resume.experience.add(experience);
    }

    public void add(Consumer consumer) {
        relatives.add(consumer);
    }

    public int getDegreeInFriendship(Consumer consumer) {
        ArrayList<Consumer> neighborsQ = new ArrayList<>();
        ArrayList<Integer> depthQ = new ArrayList<>();
        ArrayList<Consumer> visited = new ArrayList<>();

        neighborsQ.add(this);
        visited.add(this);
        depthQ.add(0);
        while (neighborsQ.size() != 0) {
            Consumer current = neighborsQ.remove(0);
            Integer depth = depthQ.remove(0);
            if (current.equals(consumer))
                return depth;
            for (Consumer neighbor : current.relatives) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    neighborsQ.add(neighbor);
                    depthQ.add(depth + 1);
                }
            }
        }
        return -1;
    }

    public void remove(Consumer consumer) {
        relatives.remove(consumer);
    }

    public Integer getGraduationYear() {
        LocalDate endDate = resume.education.stream().filter(education -> education.getLevel().equals("college"))
                .findFirst()
                .get()
                .getEndDate();
        return endDate == null ? null : endDate.getYear();
    }

    public Double meanGPA() {
        return resume.education.stream()
                .mapToDouble(Education::getGrade)
                .average().getAsDouble();
    }

    public Integer expYears() {
        if (resume.experience == null)
            return 0;

        Period period = Period.of(0, 0, 0);
        for (Experience experience : resume.experience) {
            LocalDate startDate = experience.getStartDate();
            LocalDate endDate;
            if (experience.getEndDate() == null)
                endDate = LocalDate.now();
            else
                endDate = experience.getEndDate();
            Period passed = Period.between(startDate, endDate);
            period = period.plus(passed);
        }
        int years = period.getYears();
        if (period.getMonths() >= 3)
            years++;
        return years;
    }

    public Resume getResume() {
        return resume;
    }

    public ArrayList<Consumer> getRelatives() {
        return relatives;
    }

    @Override
    public String toString() {
        return resume.toString(true);
    }

    public String toJSON() {
        return resume.toJSON(true);
    }

    public static class Resume {
        private final Information info;
        private final TreeSet<Education> education;
        private final TreeSet<Experience> experience;

        public Resume(ResumeBuilder builder) {
            info = builder.info;
            education = builder.education;
            experience = builder.experience;
        }

        public Information getInfo() {
            return info;
        }

        public TreeSet<Education> getEducation() {
            return education;
        }

        public TreeSet<Experience> getExperience() {
            return experience;
        }

        public String toString(boolean withInfo) {
            StringBuilder stringBuilder = new StringBuilder();
            if (withInfo)
                stringBuilder.append(info.toString());

            stringBuilder.append("Education: \n");
            for (Education ed : education) {
                stringBuilder.append(ed.toString());
                stringBuilder.append("\n");
            }

            stringBuilder.append("Experience: \n");
            for (Experience xp : experience) {
                stringBuilder.append(xp.toString());
                stringBuilder.append("\n");
            }
            return stringBuilder.toString();
        }

        public String toJSON(boolean withInfo) {
            StringBuilder stringBuilder = new StringBuilder();
            if (withInfo)
                stringBuilder.append(info.toJSON());

            int index = 0;
            int size = education.size();
            stringBuilder.append("\t\t\t\"education\": [\n");
            for (Education ed : education) {
                stringBuilder.append(ed.toJSON());
                if (index != size - 1)
                    stringBuilder.append(",");
                index++;
                stringBuilder.append("\n");
            }
            stringBuilder.append("\t\t\t],\n");

            stringBuilder.append("\t\t\t\"experience\": [\n");
            if (experience != null) {
                index = 0;
                size = experience.size();

                for (Experience xp : experience) {
                    stringBuilder.append(xp.toJSON());
                    if (index != size - 1)
                        stringBuilder.append(",");
                    index++;
                    stringBuilder.append("\n");
                }
            }
            stringBuilder.append("\t\t\t]\n");
            return stringBuilder.toString();
        }

        public static class ResumeBuilder {
            private Information info;
            private TreeSet<Education> education;
            private TreeSet<Experience> experience;

            public ResumeBuilder info(Information info) {
                this.info = info;
                return this;
            }

            public ResumeBuilder education(Education education) {
                if (this.education == null)
                    this.education = new TreeSet<>();
                this.education.add(education);
                return this;
            }

            public ResumeBuilder experience(Experience experience) {
                if (this.experience == null)
                    this.experience = new TreeSet<>();
                this.experience.add(experience);
                return this;
            }

            public Resume build() {
                try {
                    if (info == null || education == null)
                        throw new ResumeIncompleteException();
                    return new Resume(this);
                } catch (ResumeIncompleteException e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                    System.exit(1);
                }
                return null;
            }
        }
    }
}
