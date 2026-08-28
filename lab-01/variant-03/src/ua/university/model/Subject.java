package ua.university.model;

import java.util.Objects;

public class Subject {

    public static final int MIN_CREDITS = 1;
    public static final int MAX_CREDITS = 20;

    private String title;
    private int credits;

    public Subject(String title, int credits) {
        setTitle(title);
        setCredits(credits);
    }

    public static Subject of(String title, int credits) {
        return new Subject(title, credits);
    }

    public String getTitle() { return title; }
    public int getCredits()  { return credits; }

    public final void setTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title can not be empty");
        }
        this.title = title.strip();
    }

    public final void setCredits(int credits) {
        if (credits < MIN_CREDITS || credits > MAX_CREDITS) {
            throw new IllegalArgumentException(
                    "credits should be in range [%d..%d], but now: %d"
                            .formatted(MIN_CREDITS, MAX_CREDITS, credits));
        }
        this.credits = credits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return credits == subject.credits && title.equals(subject.title);
    }

    @Override
    public String toString() {
        return "Subject{title='%s', credits=%d}".formatted(title, credits);
    }
}
