import React from 'react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';

const dummyData = [
  { name: 'Jan', value: 400 },
  { name: 'Feb', value: 300 },
  { name: 'Mar', value: 550 },
  { name: 'Apr', value: 200 },
  { name: 'May', value: 700 },
];

const DataChart = () => {
  return (
    <div className="bg-white p-6 rounded-xl border border-gray-100 shadow-sm h-80">
      <h3 className="text-lg font-bold text-gray-900 mb-6">Data Overview</h3>
      <ResponsiveContainer width="100%" height="100%">
        <BarChart data={dummyData}>
          <CartesianGrid strokeDasharray="3 3" vertical={false} />
          <XAxis dataKey="name" axisLine={false} tickLine={false} />
          <YAxis axisLine={false} tickLine={false} />
          <Tooltip cursor={{fill: '#f3f4f6'}} />
          <Bar dataKey="value" fill="#3b82f6" radius={[4, 4, 0, 0]} />
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
};

export default DataChart;
