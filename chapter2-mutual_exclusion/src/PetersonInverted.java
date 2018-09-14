class PetersonInverted implements Lock {
    boolean wantCS[] = {false, false};
    int turn = 1;
    public void requestCS(int i) {
        int j = 1 - i;
        turn = j;
        wantCS[i] = true;
        while (wantCS[j] && (turn == j)) ;
    }
    public void releaseCS(int i) {
        wantCS[i] = false;
    }
}
