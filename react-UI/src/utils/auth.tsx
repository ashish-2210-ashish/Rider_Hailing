import { jwtDecode } from "jwt-decode";

type payload = {
    sub : string;
    role : string;
    iat : number;
    exp : number;
}

export function getUserRole() : void{
    const session = sessionStorage.getItem("session");
    if (!session){
        return ;
    }

    try{
        const token = JSON.parse(session);
        const decoded:payload=jwtDecode(token.token);
        sessionStorage.setItem("userDetails",JSON.stringify(decoded));
        console.log(decoded.sub);
        console.log(decoded.role)
        return ;
    }
    catch (error) {
        console.error("Error decoding token:", error);
        return ;
    }
}