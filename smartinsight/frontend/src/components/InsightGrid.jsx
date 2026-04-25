import React from 'react';
import InsightCard from './InsightCard';

const InsightGrid = ({ insightsData }) => {
  if (!insightsData) return null;

  const insightsList = insightsData.content ? insightsData.content.split('\n').filter(i => i.trim() !== '') : [];

  return (
    <div className="space-y-6">
      <div className="bg-white p-6 rounded-xl border border-gray-100 shadow-sm">
        <h2 className="text-lg font-bold text-gray-900 mb-2">Executive Summary</h2>
        <p className="text-gray-700">{insightsData.executiveSummary}</p>
      </div>
      
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
        {insightsList.map((insight, index) => (
          <InsightCard 
            key={index}
            title={`Key Insight ${index + 1}`}
            content={insight.replace(/^-\s*/, '')}
            isAnomaly={insightsData.anomalyFlag && index === 0} 
          />
        ))}
      </div>
    </div>
  );
};

export default InsightGrid;
