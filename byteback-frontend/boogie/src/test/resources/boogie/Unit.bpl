type Unit;

const unique unit : Unit;

function identity<T>(arg: T) returns (T);

procedure nothing<T>(arg: T) {}

procedure identity<T>(arg: T) returns (ret: T) {
    var identity: T;
    identity := arg;
    ret := identity;
}
