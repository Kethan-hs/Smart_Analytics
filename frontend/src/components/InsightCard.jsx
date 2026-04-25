import React from 'react';
import { AlertTriangle, Lightbulb } from 'lucide-react';

const InsightCard = ({ title, content, isAnomaly }) => {
  return (
    <div className={`p-6 rounded-xl border ${isAnomaly ? 'bg-orange-50 border-orange-200' : 'bg-white border-gray-100 shadow-sm'}`}>
      <div className="flex items-start gap-3">
        <div className={`p-2 rounded-lg ${isAnomaly ? 'bg-orange-100' : 'bg-blue-50'}`}>
          {isAnomaly ? (
            <AlertTriangle className="h-5 w-5 text-orange-600" />
          ) : (
            <Lightbulb className="h-5 w-5 text-blue-600" />
          )}
        </div>
        <div>
          <h3 className="font-semibold text-gray-900 mb-1">{title}</h3>
          <p className="text-gray-600 text-sm leading-relaxed">{content}</p>
        </div>
      </div>
    </div>
  );
};

export default InsightCard;
