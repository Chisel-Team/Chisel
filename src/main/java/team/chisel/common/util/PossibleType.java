package team.chisel.common.util;

/**
 * Class that can either have an object of type 2, but not both
 */
public class PossibleType<FIRST, SECOND> {

    private FIRST first;

    private SECOND second;

    private boolean isFirst;


    public static <FIRST, SECOND> PossibleType<FIRST, SECOND> makeFirst(FIRST first){
        return new PossibleType<FIRST, SECOND>(first);
    }

    public static <FIRST, SECOND> PossibleType<FIRST, SECOND> makeSecond(SECOND second){
        return new PossibleType<FIRST, SECOND>(second, (byte)0);
    }

    private PossibleType(FIRST first) {
        this.first = first;
        this.isFirst = true;
    }

    private PossibleType(SECOND second, byte b){
        this.second = second;
        this.isFirst = false;
    }

    public FIRST getFirst(){
        return this.first;
    }

    public SECOND getSecond(){
        return this.second;
    }

    public boolean isFirst(){
        return this.isFirst;
    }

    public boolean isSecond(){
        return !isFirst();
    }
}
