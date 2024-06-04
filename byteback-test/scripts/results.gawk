func parse_goal(vc) {
    gsub(/'vc\.$/, "", vc);
    orig=vc;

    gsub(/'6/, ";", vc);
    gsub(/_/, ".", vc);
    gsub(/_/, ".", vc);
    gsub(/[Ii]'2/, "", vc);
    vc=gensub(/'5(.*)'5/, "<\\1>", "g", vc);
    vc=gensub(/'7(.*)'7/, "(\\1)", "g", vc);
    gsub(/'3/, "_", vc);
    gsub(/'4/, "$", vc);
    gsub(/'9/, "'", vc);
    gsub(/^Default'8./, "", vc);
    goal=vc;
}

BEGIN {
    IFS=" ";
    OFS=",";
    "pwd" | getline pwd;
    pwd=pwd "/";
    print "file","kind","goal","result","time","steps";
}
/^File/ {
    gsub(pwd, "", $2);
    gsub(/:$/, "", $2);
    file=$2;
}
/^Goal / {
    parse_goal($2);
}
# Why3 prints the goal name on the next line if it is too long, fetch it
/^Goal$/ {
    getline;
    parse_goal($0);
}
/^Prover/ {
    result=gensub(/^Prover result is: (.*) \(.*$/, "\\1", "g", $0);
    time=gensub(/^.*\(([0-9\.]+)s.*$/, "\\1", "g", $0);
    steps=gensub(/(.*, ([0-9]+) steps\))?.*/, "\\2", "g", $0);

    cmd="scripts/decltype.sh \"" file "\" \"" orig "\"";
    cmd | getline type;
    close(cmd);

    print file,type,goal,result,time,steps;
}
