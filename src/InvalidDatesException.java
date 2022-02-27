class InvalidDatesException extends Exception {
    public InvalidDatesException() {
        super("InvalidDateOrder exception thrown; make sure that startDate <= endDate");
    }
}