"use client";

import { useEffect, useState } from "react";
import { api } from "@/lib/api";

export default function AccessGuard({ children }) {
  const [allowed, setAllowed] = useState(null);

  useEffect(() => {
    const token = new URLSearchParams(window.location.search).get("token");

    if (!token) {
      setAllowed(false);
      return;
    }

    api
      .get(`/access/validate?token=${token}`)
      .then(res => setAllowed(res.data))
      .catch(() => setAllowed(false));
  }, []);

  if (allowed === null) {
    return <div className="min-h-screen flex items-center justify-center">...</div>;
  }

  if (!allowed) {
    return (
      <div className="min-h-screen flex items-center justify-center text-center px-6">
        <p className="text-gray-400">
          This space is private.  
          <br />
          It exists only for those it was meant for.
        </p>
      </div>
    );
  }

  return children;
}
