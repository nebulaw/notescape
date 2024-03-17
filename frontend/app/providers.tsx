import { AuthProvider } from "@/contexts/auth-context";
import { ClientCookiesProvider } from "./cookies";
import { cookies } from "next/headers";

export function Providers({ children }: React.PropsWithChildren) {
  return (
    <ClientCookiesProvider value={cookies().getAll()}>
      <AuthProvider>
        {children}
      </AuthProvider>
    </ClientCookiesProvider>
  );
}


