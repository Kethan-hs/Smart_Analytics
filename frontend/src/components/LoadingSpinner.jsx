import React from 'react';
import { Loader2 } from 'lucide-react';

const LoadingSpinner = ({ message = "Processing data..." }) => {
  return (
    <div className="flex flex-col items-center justify-center p-12">
      <Loader2 className="h-10 w-10 text-blue-500 animate-spin mb-4" />
      <p className="text-gray-600 font-medium">{message}</p>
    </div>
  );
};

export default LoadingSpinner;
